package com.example.doan_nhom9_sang6.PhamNam_Activity.QuenMatKhau.Email

import java.util.Properties
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailSender(
    private val username: String, // Email gửi đi
    private val password: String  // mk app
) {
    fun sendEmail(recipient: String, subject: String, messageBody: String) {
        val props = Properties().apply {
            put("mail.smtp.host", "smtp.gmail.com") //  Địa chỉ máy chủ SMTP của Gmail
            put("mail.smtp.port", "587")  // Sử dụng cổng 587 cho STARTTLS
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")  // Kích hoạt STARTTLS để bảo mật kết nối.
        }

        val session = Session.getInstance(props, object : javax.mail.Authenticator() { //Tạo một phiên làm việc với cấu hình trong props.
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(username)) //  Địa chỉ email của người gửi.
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient)) // Địa chỉ email người nhận
                this.subject = subject // tiêu đề email
                setText(messageBody)
            }
            Transport.send(message) // Thực hiện gửi email thông qua giao thức SMTP.
        } catch (e: Exception) {
            println("Lỗi : ${e.message}")
            e.printStackTrace()
        }
    }
}
