package com.example.laba4

import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.SpringLayout
import javax.swing.SwingConstants

class MainWindow: JFrame("Лаба 4") {

    private var controller: DataBaseController = DataBaseController()

    private lateinit var loginPanel: JPanel
    private lateinit var guestPanel: JPanel
    private lateinit var userPanel: JPanel

    private val listOfUsers = JLabel()
    private val usersLabel = JLabel()
    private var selector = JComboBox(arrayOf(""))

    private var role = Roles.NONE

    private var username = ""
    private var password = ""

    private var usersList = controller.getUsersFromTable()

    init {

        setBounds(0, 0, 800, 630)
        setDefaultLookAndFeelDecorated(true)

        setLoginPanel()
        setGuestPanel()
        setUserPanel()

        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true



    }

    private fun setLoginPanel() {
        loginPanel = JPanel()
        loginPanel.setSize(800, 600)

        //поля входа
        val usernameLabel = JLabel(" Логин")
        val passwordLabel = JLabel("Пароль")
        val usernameField = JTextField()
        val passwordField = JTextField()
        loginPanel.add(usernameLabel)
        loginPanel.add(passwordLabel)
        loginPanel.add(usernameField)
        loginPanel.add(passwordField)

        // кнопка входа как гость
        val guestButton = JButton("Войти как гость")
        guestButton.addActionListener {
            add(guestPanel)
            loginPanel.isVisible = false
            guestPanel.isVisible = true
            role = Roles.GUEST
            listOfUsers.text = getUsersLabel()

        }
        loginPanel.add(guestButton)

        // кнопка входа как пользователь с правами редактирования
        val userButton = JButton("Войти как пользователь")
        userButton.addActionListener {
            username = usernameField.text.toString()
            password = passwordField.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                role = controller.getUserRole(username, password)
                if (role != Roles.NONE) {

                    usernameField.text = ""
                    passwordField.text = ""

                    loginPanel.isVisible = false

                    if (role == Roles.SECRETARY) {
                        add(guestPanel)
                        guestPanel.isVisible = true
                        listOfUsers.text = getUsersLabel()
                    } else {
                        add(userPanel)
                        userPanel.isVisible = true

                        updateInfo()

                    }

                }
            }

        }
        loginPanel.add(userButton)


        add(loginPanel)



        val layout = SpringLayout()
        loginPanel.layout = layout

        layout.putConstraint(SpringLayout.WEST, usernameLabel, 20, SpringLayout.WEST, loginPanel)
        layout.putConstraint(SpringLayout.NORTH, usernameLabel, 20, SpringLayout.NORTH, loginPanel)

        layout.putConstraint(SpringLayout.WEST, passwordLabel, 20, SpringLayout.WEST, loginPanel)
        layout.putConstraint(SpringLayout.NORTH, passwordLabel, 20, SpringLayout.SOUTH, usernameLabel)

        layout.putConstraint(SpringLayout.WEST, usernameField, 20, SpringLayout.EAST, usernameLabel)
        layout.putConstraint(SpringLayout.NORTH, usernameField, -5, SpringLayout.NORTH, usernameLabel)

        layout.putConstraint(SpringLayout.WEST, passwordField, 0, SpringLayout.WEST, usernameField)
        layout.putConstraint(SpringLayout.NORTH, passwordField, -5, SpringLayout.NORTH, passwordLabel)

        layout.putConstraint(SpringLayout.WEST, guestButton, 10, SpringLayout.WEST, loginPanel)
        layout.putConstraint(SpringLayout.NORTH, guestButton, 20, SpringLayout.SOUTH, passwordLabel)

        layout.putConstraint(SpringLayout.WEST, userButton, 10, SpringLayout.EAST, guestButton)
        layout.putConstraint(SpringLayout.NORTH, userButton, 20, SpringLayout.SOUTH, passwordLabel)


        layout.putConstraint(SpringLayout.EAST, usernameField, 0, SpringLayout.EAST, userButton)
        layout.putConstraint(SpringLayout.EAST, passwordField, 0, SpringLayout.EAST, userButton)


    }

    private fun setGuestPanel() {
        guestPanel = JPanel()
        guestPanel.setSize(800, 600)
        guestPanel.layout = null


        // список сотрудников
        guestPanel.add(listOfUsers)
        listOfUsers.setBounds(10, 10, 650, 600)
        listOfUsers.verticalAlignment = SwingConstants.NORTH

        // кнопка возврата
        val returnButton = JButton("Выйти")
        returnButton.addActionListener {
            remove(guestPanel)
            guestPanel.isVisible = false
            loginPanel.isVisible = true
            role = Roles.NONE
            username = ""
            password = ""
        }
        returnButton.setBounds(610, 10, 130, 50)


        guestPanel.add(returnButton)


    }

    private fun setUserPanel() {

        userPanel = JPanel()
        userPanel.setSize(800, 600)
        userPanel.layout = null
        userPanel.isVisible = false





        // список пользователей
        usersLabel.setBounds(10, 10, 650, 530)
        usersLabel.verticalAlignment = SwingConstants.NORTH
        userPanel.add(usersLabel)



        // поля ввода
        val fullNameField = JTextField()
        fullNameField.setBounds(10, 540, 150, 50)
        userPanel.add(fullNameField)

        val jobField = JComboBox(arrayOf("Директор", "Зам. директора", "Секретарь"))
        jobField.setBounds(170, 540, 100, 50)
        userPanel.add(jobField)

        val addressField = JTextField()
        addressField.setBounds(280, 540, 100, 50)
        userPanel.add(addressField)

        val phoneNumberField = JTextField()
        phoneNumberField.setBounds(390, 540, 100, 50)
        userPanel.add(phoneNumberField)

        val usernameField = JTextField()
        usernameField.setBounds(500, 540, 100, 50)
        userPanel.add(usernameField)

        val passwordField = JTextField()
        passwordField.setBounds(610, 540, 100, 50)
        userPanel.add(passwordField)

        val applyButton = JButton("Создать")
        applyButton.setBounds(720, 540, 70, 50)
        applyButton.addActionListener {

            if (fullNameField.text.isNotEmpty() && addressField.text.isNotEmpty() && phoneNumberField.text.isNotEmpty() && usernameField.text.isNotEmpty() && passwordField.text.isNotEmpty()){

                val user = UserData(
                    0,
                    fullNameField.text.toString(),
                    arrayOf("Директор", "Зам. директора", "Секретарь").indexOf(jobField.selectedItem),
                    addressField.text.toString(),
                    phoneNumberField.text.toString(),
                    usernameField.text.toString(),
                    passwordField.text.toString()
                )

                if (selector.selectedIndex == 0 && role == Roles.DIRECTOR) { // добавить
                    controller.addNewUser(user)
                    updateInfo()

                } else if (selector.selectedIndex > 0) { // обновить
                    controller.updateUserById(usersList[selector.selectedIndex - 1].id, user)
                    updateInfo()
                }

            }



        }
        userPanel.add(applyButton)



        // кнопка возврата
        val returnButton = JButton("Выйти")
        returnButton.addActionListener {
            remove(userPanel)
            userPanel.isVisible = false
            loginPanel.isVisible = true
            role = Roles.NONE
            username = ""
            password = ""
        }
        returnButton.setBounds(660, 10, 130, 50)
        userPanel.add(returnButton)

        // кнопка удаления
        val deleteButton = JButton("Удалить")
        deleteButton.addActionListener {
            if (role == Roles.DIRECTOR && selector.selectedIndex > 0) {
                controller.deleteUserById(usersList[selector.selectedIndex - 1].id)

                updateInfo()
            }
        }
        deleteButton.setBounds(660, 70, 130, 50)
        userPanel.add(deleteButton)





        // выбор сотрудника
        selector.addActionListener {
            if (selector.selectedIndex > 0) {
                val selectedUser = usersList[selector.selectedIndex - 1]

                fullNameField.text = selectedUser.fullName
                jobField.selectedIndex = selectedUser.job
                addressField.text = selectedUser.address
                phoneNumberField.text = selectedUser.phoneNumber
                usernameField.text = selectedUser.username
                passwordField.text = selectedUser.password

                applyButton.text = "Обновить"

            } else if (selector.selectedIndex == 0) {
                fullNameField.text = ""
                jobField.selectedIndex = 0
                addressField.text = ""
                phoneNumberField.text = ""
                usernameField.text = ""
                passwordField.text = ""

                applyButton.text = "Добавить"

            }


        }
        selector.setBounds(660, 130, 130, 50)
        userPanel.add(selector)


    }

    private fun getSelectorData(usersList: MutableList<UserData>): Array<String> {
        val data = mutableListOf("Выберите пользователя")
        for (user in usersList) {
            data += user.fullName
        }

        return data.toTypedArray()
    }

    private fun getUsersLabel(): String {
        var text = ""
        if (role != Roles.NONE) {
            val roles = arrayOf("Директор", "Зам. директора", "Секретарь", "Гость")
            val roleText = roles[role.ordinal]

            if (role == Roles.GUEST) {
                updateUsersList()
                text =
                    "<html>" +
                            "<b>Вы вошли как $roleText!</b><br><br>" +
                            "<u>Список сотрудников:</u><br>"
                for (user in usersList) {
                    val job = roles[user.job]
                    text +=
                        "<b>Имя:</b> ${user.fullName}; <b>Должность:</b> $job;<br>"
                }
                text += "</html>"
            } else {
                updateUsersList()
                text =
                    "<html>" +
                            "<b>Вы вошли как $roleText!</b><br><br>" +
                            "<u>Список сотрудников:</u><br>"
                for (user in usersList) {
                    val job = roles[user.job]
                    text += "<b>Имя:</b> ${user.fullName}; <b>Должность:</b> $job; <b>Адрес:</b> ${user.address}; <b>Номер телефона:</b> ${user.phoneNumber};<br>"
                }
                text += "</html>"
            }
        }


        return text
    }

    private fun updateUsersList() {
        usersList = controller.getUsersFromTable()
    }

    private fun updateSelector() {
        val newItems = getSelectorData(usersList)
        for (i in 0 until selector.itemCount) {
            selector.removeItemAt(0)
        }
        for (item in newItems) {
            selector.addItem(item)
        }
        selector.selectedIndex = 0
    }

    private fun updateInfo() {
        updateUsersList()
        updateSelector()
        usersLabel.text = getUsersLabel()
    }

}