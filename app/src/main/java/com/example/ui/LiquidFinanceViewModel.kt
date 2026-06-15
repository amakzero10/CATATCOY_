package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// ==========================================
// UI STATE REPRESENTATION
// ==========================================

data class LiquidFinanceUiState(
    val transactions: List<TransactionEntity> = emptyList(),
    val filteredTransactions: List<TransactionEntity> = emptyList(),
    val budgets: List<BudgetEntity> = emptyList(),
    val savingsGoals: List<SavingsGoalEntity> = emptyList(),
    val notedItems: List<NotedEntity> = emptyList(),
    val categories: List<CategoryEntity> = emptyList(),
    
    // Aggregates
    val totalBalance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    
    // Selection Filters
    val selectedTypeFilter: String = "ALL", // "ALL", "INCOME", "EXPENSE"
    val selectedCategoryFilter: String = "ALL", // "ALL" or category name
    
    // UI Dialog States
    val showAddTransactionDialog: Boolean = false,
    val showAddBudgetDialog: Boolean = false,
    val showAddSavingsGoalDialog: Boolean = false,
    val showContributionDialog: Boolean = false,
    val showAddNotedDialog: Boolean = false,
    val targetGoalForContribution: SavingsGoalEntity? = null,
    val editingTransaction: TransactionEntity? = null
)

data class CombinedDbData(
    val txList: List<TransactionEntity>,
    val budgetList: List<BudgetEntity>,
    val savingsList: List<SavingsGoalEntity>,
    val notedList: List<NotedEntity>,
    val categoryList: List<CategoryEntity>
)

class LiquidFinanceViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FinanceDatabase.getDatabase(application)
    private val transactionDao = database.transactionDao()
    private val budgetDao = database.budgetDao()
    private val savingsGoalDao = database.savingsGoalDao()
    private val notedDao = database.notedDao()
    private val categoryDao = database.categoryDao()

    private val _uiState = MutableStateFlow(LiquidFinanceUiState())
    val uiState: StateFlow<LiquidFinanceUiState> = _uiState.asStateFlow()

    init {
        observeDatabase()
        ensureDefaultCategories()
    }

    private fun ensureDefaultCategories() {
        viewModelScope.launch {
            if (categoryDao.getCategoryCount() == 0) {
                val defaultCategories = listOf(
                    CategoryEntity("Gaji", "INCOME", false),
                    CategoryEntity("Investasi", "INCOME", false),
                    CategoryEntity("Freelance", "INCOME", false),
                    CategoryEntity("Lainnya", "INCOME", false),
                    CategoryEntity("Makanan", "EXPENSE", false),
                    CategoryEntity("Belanja", "EXPENSE", false),
                    CategoryEntity("Rumah", "EXPENSE", false),
                    CategoryEntity("Transport", "EXPENSE", false),
                    CategoryEntity("Hiburan", "EXPENSE", false),
                    CategoryEntity("Lainnya", "EXPENSE", false)
                )
                defaultCategories.forEach { categoryDao.insertCategory(it) }
            }
        }
    }

    private fun observeDatabase() {
        // Combine all DB flows into synchronized UI state
        viewModelScope.launch {
            combine(
                transactionDao.getAllTransactions(),
                budgetDao.getAllBudgets(),
                savingsGoalDao.getAllSavingsGoals(),
                notedDao.getAllNotedItems(),
                categoryDao.getAllCategories()
            ) { tx, budget, savings, noted, cats ->
                CombinedDbData(tx, budget, savings, noted, cats)
            }.collect { data ->
                val txList = data.txList
                val budgetList = data.budgetList
                val savingsList = data.savingsList
                val notedList = data.notedList
                val categoryList = data.categoryList

                // Calculate Aggregates
                val income = txList.filter { it.type == "INCOME" }.sumOf { it.amount }
                val expense = txList.filter { it.type == "EXPENSE" }.sumOf { it.amount }
                val balance = income - expense

                _uiState.update { state ->
                    val filtered = applyFilters(txList, state.selectedTypeFilter, state.selectedCategoryFilter)
                    state.copy(
                        transactions = txList,
                        filteredTransactions = filtered,
                        budgets = budgetList,
                        savingsGoals = savingsList,
                        notedItems = notedList,
                        categories = categoryList,
                        totalBalance = balance,
                        totalIncome = income,
                        totalExpense = expense
                    )
                }
            }
        }
    }

    private fun applyFilters(list: List<TransactionEntity>, type: String, category: String): List<TransactionEntity> {
        return list.filter { tx ->
            val matchesType = when (type) {
                "INCOME" -> tx.type == "INCOME"
                "EXPENSE" -> tx.type == "EXPENSE"
                else -> true
            }
            val matchesCategory = if (category == "ALL") true else tx.category.uppercase() == category.uppercase()
            matchesType && matchesCategory
        }
    }

    // ==========================================
    // SELECTION OPERATIONS
    // ==========================================

    fun setFilters(type: String, category: String) {
        _uiState.update { state ->
            val filtered = applyFilters(state.transactions, type, category)
            state.copy(
                selectedTypeFilter = type,
                selectedCategoryFilter = category,
                filteredTransactions = filtered
            )
        }
    }

    // ==========================================
    // DIALOG CONTROL
    // ==========================================

    fun setTransactionDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showAddTransactionDialog = visible) }
    }

    fun setEditingTransaction(tx: TransactionEntity?) {
        _uiState.update { it.copy(editingTransaction = tx) }
    }

    fun updateTransaction(tx: TransactionEntity) {
        viewModelScope.launch {
            transactionDao.insertTransaction(tx)
        }
    }

    fun setBudgetDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showAddBudgetDialog = visible) }
    }

    fun setSavingsGoalDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showAddSavingsGoalDialog = visible) }
    }
    
    fun setContributionDialogVisible(visible: Boolean, goal: SavingsGoalEntity? = null) {
        _uiState.update { 
            it.copy(
                showContributionDialog = visible,
                targetGoalForContribution = goal
            )
        }
    }

    // ==========================================
    // WRITE OPERATIONS
    // ==========================================

    fun addTransaction(title: String, amount: Double, type: String, category: String, notes: String = "") {
        viewModelScope.launch {
            val transaction = TransactionEntity(
                title = title.ifBlank { if (type == "INCOME") "Untranslated Income" else "Untranslated Expense" },
                amount = amount,
                type = type,
                category = category,
                timestamp = System.currentTimeMillis(),
                notes = notes
            )
            transactionDao.insertTransaction(transaction)
            
            // Auto update budget limit if expense category has no budget (create a loose default limit of 6,000 for representation)
            // Or just insert it.
        }
    }

    fun deleteTransaction(tx: TransactionEntity) {
        viewModelScope.launch {
            transactionDao.deleteTransaction(tx)
        }
    }

    fun addBudget(category: String, limit: Double) {
        viewModelScope.launch {
            budgetDao.insertBudget(BudgetEntity(category = category, monthlyLimit = limit))
        }
    }

    fun deleteBudget(budget: BudgetEntity) {
        viewModelScope.launch {
            budgetDao.deleteBudget(budget)
        }
    }

    fun addSavingsGoal(title: String, target: Double, current: Double) {
        viewModelScope.launch {
            val goal = SavingsGoalEntity(title = title, targetAmount = target, currentAmount = current)
            savingsGoalDao.insertSavingsGoal(goal)
        }
    }

    fun updateSavingsGoalProgress(goal: SavingsGoalEntity, addition: Double) {
        viewModelScope.launch {
            val updated = goal.copy(currentAmount = (goal.currentAmount + addition).coerceIn(0.0, goal.targetAmount))
            savingsGoalDao.updateSavingsGoal(updated)
        }
    }

    fun deleteSavingsGoal(goal: SavingsGoalEntity) {
        viewModelScope.launch {
            savingsGoalDao.deleteSavingsGoal(goal)
        }
    }

    fun setNotedDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showAddNotedDialog = visible) }
    }

    fun addNotedItem(title: String, nominal: Double, isDebt: Boolean, notes: String = "", dueDate: String = "") {
        viewModelScope.launch {
            val item = NotedEntity(
                title = title.ifBlank { if (isDebt) "Utang Baru" else "Piutang Baru" },
                nominal = nominal,
                isCompleted = false,
                dueDate = dueDate,
                notes = notes,
                isDebt = isDebt
            )
            notedDao.insertNotedItem(item)
        }
    }

    fun addCategory(name: String, type: String) {
        viewModelScope.launch {
            categoryDao.insertCategory(CategoryEntity(name = name, type = type, isCustom = true))
        }
    }

    fun deleteCategory(cat: CategoryEntity) {
        viewModelScope.launch {
            categoryDao.deleteCategory(cat)
        }
    }

    fun toggleNotedItemCompleted(item: NotedEntity) {
        viewModelScope.launch {
            notedDao.insertNotedItem(item.copy(isCompleted = !item.isCompleted))
        }
    }

    fun deleteNotedItem(item: NotedEntity) {
        viewModelScope.launch {
            notedDao.deleteNotedItem(item)
        }
    }

    fun resetDataToDefaults() {
        viewModelScope.launch {
            transactionDao.clearAllTransactions()
            categoryDao.clearAllCategories()
            ensureDefaultCategories()
            prePopulateData()
        }
    }

    // ==========================================
    // FIRST-LAUNCH PRE-POPULATION SOURCE DATA
    // ==========================================

    private suspend fun prePopulateData() {
        val now = System.currentTimeMillis()
        val oneDayMillis = 86400000L

        // Initial Transactions (Beautiful mock tracking values for representation)
        val defaultTx = listOf(
            TransactionEntity(title = "Gaji Kuartal Utama", amount = 42500000.0, type = "INCOME", category = "Gaji", timestamp = now),
            TransactionEntity(title = "Dividen Saham GOTO", amount = 8200000.0, type = "INCOME", category = "Investasi", timestamp = now - (oneDayMillis * 1)),
            TransactionEntity(title = "Sewa Apartemen Bulanan", amount = 12000000.0, type = "EXPENSE", category = "Rumah", timestamp = now - (oneDayMillis * 2)),
            TransactionEntity(title = "Belanja Bulanan Supermarket", amount = 3450000.0, type = "EXPENSE", category = "Belanja", timestamp = now - (oneDayMillis * 3)),
            TransactionEntity(title = "Fine Dining Jakarta", amount = 1850000.0, type = "EXPENSE", category = "Makanan", timestamp = now - (oneDayMillis * 4)),
            TransactionEntity(title = "Desain Freelance UI/UX", amount = 6500000.0, type = "INCOME", category = "Freelance", timestamp = now - (oneDayMillis * 5)),
            TransactionEntity(title = "Service Rutin Mobil", amount = 2200000.0, type = "EXPENSE", category = "Transport", timestamp = now - (oneDayMillis * 6))
        )

        defaultTx.forEach { transactionDao.insertTransaction(it) }

        // Initial Budgets Setup
        val defaultBudgets = listOf(
            BudgetEntity("Rumah", 15000000.0),
            BudgetEntity("Belanja", 6000000.0),
            BudgetEntity("Makanan", 4500000.0),
            BudgetEntity("Transport", 3000000.0)
        )
        defaultBudgets.forEach { budgetDao.insertBudget(it) }

        // Initial Savings Goals setup
        val defaultGoals = listOf(
            SavingsGoalEntity(title = "Dana Darurat (Emergency)", targetAmount = 50000000.0, currentAmount = 35000000.0),
            SavingsGoalEntity(title = "Investasi Crypto (BTC/ETH)", targetAmount = 25000000.0, currentAmount = 14200000.0),
            SavingsGoalEntity(title = "Liburan ke Tokyo 2027", targetAmount = 30000000.0, currentAmount = 9000000.0)
        )
        defaultGoals.forEach { savingsGoalDao.insertSavingsGoal(it) }
    }
}
