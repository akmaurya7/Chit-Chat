package com.example.chitchat

import com.google.firebase.firestore.FirebaseFirestore



//// Function to send a message
//fun sendMessage(chatId: String, senderId: String, text: String) {
//    val db = FirebaseFirestore.getInstance()
//    val message = hashMapOf(
//        "senderId" to senderId,
//        "text" to text,
//        "timestamp" to System.currentTimeMillis()
//    )
//
//    db.collection("chats").document(chatId).collection("messages")
//        .add(message)
//        .addOnSuccessListener {
//            println("Message sent successfully!")
//        }
//        .addOnFailureListener { e ->
//            println("Error sending message: $e")
//        }
//}
//
//// Function to listen for new messages in a chat
//fun listenForMessages(chatId: String) {
//    db.collection("chats").document(chatId).collection("messages")
//        .orderBy("timestamp")
//        .addSnapshotListener { snapshots, e ->
//            if (e != null) {
//                println("Listen failed: $e")
//                return@addSnapshotListener
//            }
//
//            for (doc in snapshots!!) {
//                println("${doc.id} => ${doc.data}")
//            }
//        }
//}
//
//// Function to create a new chat
//fun createChat(participants: List<String>) {
//    val chat = hashMapOf(
//        "participants" to participants,
//        "lastMessage" to "",
//        "timestamp" to System.currentTimeMillis()
//    )
//
//    db.collection("chats")
//        .add(chat)
//        .addOnSuccessListener { documentReference ->
//            println("Chat created with ID: ${documentReference.id}")
//        }
//        .addOnFailureListener { e ->
//            println("Error creating chat: $e")
//        }
//}
