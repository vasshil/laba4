package com.example.laba4

import java.awt.BorderLayout
import java.awt.Color
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

//    private lateinit var usernameField: JTextField
//    private lateinit var passwordField: JTextField

    private var listOfUsers = JLabel()

    private var role = Roles.NONE

    private var username = ""
    private var password = ""

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
            setListOfUsers()

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
                        setListOfUsers()
                    } else {
                        add(userPanel)
                        userPanel.isVisible = true

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
        val usersContainer = JPanel()
        usersContainer.setBounds(10, 10, 650, 530)
        userPanel.add(usersContainer)


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
                val user = UserData(0,
                    fullNameField.text.toString(),
                    arrayOf("Директор", "Зам. директора", "Секретарь").indexOf(jobField.selectedItem),
                    addressField.text.toString(),
                    phoneNumberField.text.toString(),
                    usernameField.text.toString(),
                    passwordField.text.toString()
                )
                controller.addNewUser(user)
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

        // кнопка создания
        val createButton = JButton("Создать")
        createButton.addActionListener {

        }
        createButton.setBounds(660, 70, 130, 50)
        userPanel.add(createButton)

    }

//    private fun getNewUserPanel():  {
//
//    }

    private fun setListOfUsers() {
        if (role == Roles.GUEST) {
            val usersList = controller.getUsersFromTable()
            var text =
                "<html>" +
                        "<b>Вы вошли как гость!</b><br><br>" +
                        "<u>Список сотрудников:</u><br>"
            for (user in usersList) {
                val job = arrayOf("Директор", "Зам. директора", "Секретарь")[user.job]
                text +=
                    "<b>Имя:</b> ${user.fullName}; <b>Должность:</b> $job;<br>"
            }
            text += "</html>"
            listOfUsers.text = text
        } else if (role == Roles.SECRETARY) {
            val usersList = controller.getUsersFromTable()
            var text =
                "<html>" +
                        "<b>Вы вошли как секретарь!</b><br><br>" +
                        "<u>Список сотрудников:</u><br>"
            for (user in usersList) {
                val job = arrayOf("Директор", "Зам. директора", "Секретарь")[user.job]
                text += "<b>Имя:</b> ${user.fullName}; <b>Должность:</b> $job; <b>Адрес:</b> ${user.address}; <b>Номер телефона:</b> ${user.phoneNumber};<br>"
            }
            text += "</html>"
            listOfUsers.text = text
        }
    }

}