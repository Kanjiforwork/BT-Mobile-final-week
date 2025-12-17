package com.example.bai2.data.repository

import com.example.bai2.data.entity.Appointment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime

class AppointmentRepository {
    private val database = FirebaseDatabase.getInstance("https://homework-f9a6c-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val appointmentsRef = database.getReference("appointments")

    fun getAllAppointments(): Flow<List<Appointment>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val appointments = mutableListOf<Appointment>()
                for (childSnapshot in snapshot.children) {
                    childSnapshot.getValue(Appointment::class.java)?.let { appointment ->
                        appointments.add(appointment.copy(id = childSnapshot.key ?: ""))
                    }
                }
                trySend(appointments.sortedByDescending { it.createdAt })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        appointmentsRef.addValueEventListener(listener)
        awaitClose { appointmentsRef.removeEventListener(listener) }
    }

    suspend fun insertAppointment(appointment: Appointment): String {
        val key = appointmentsRef.push().key ?: throw Exception("Failed to generate key")
        val appointmentWithId = appointment.copy(id = key)
        appointmentsRef.child(key).setValue(appointmentWithId).await()
        return key
    }

    suspend fun updateAppointment(appointment: Appointment) {
        appointmentsRef.child(appointment.id).setValue(appointment).await()
    }

    suspend fun deleteAppointment(appointment: Appointment) {
        appointmentsRef.child(appointment.id).removeValue().await()
    }

    suspend fun getAppointmentById(id: String): Appointment? {
        val snapshot = appointmentsRef.child(id).get().await()
        return snapshot.getValue(Appointment::class.java)?.copy(id = snapshot.key ?: "")
    }

    fun getAppointmentsByTimeRange(
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): Flow<List<Appointment>> = callbackFlow {
        val startTimeStr = Appointment.fromLocalDateTime(startTime)
        val endTimeStr = Appointment.fromLocalDateTime(endTime)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val appointments = mutableListOf<Appointment>()
                for (childSnapshot in snapshot.children) {
                    childSnapshot.getValue(Appointment::class.java)?.let { appointment ->
                        if (appointment.fromTime >= startTimeStr && appointment.fromTime <= endTimeStr) {
                            appointments.add(appointment.copy(id = childSnapshot.key ?: ""))
                        }
                    }
                }
                trySend(appointments.sortedByDescending { it.createdAt })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        appointmentsRef.addValueEventListener(listener)
        awaitClose { appointmentsRef.removeEventListener(listener) }
    }

    fun getUnsyncedNotifications(): Flow<List<Appointment>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val appointments = mutableListOf<Appointment>()
                for (childSnapshot in snapshot.children) {
                    childSnapshot.getValue(Appointment::class.java)?.let { appointment ->
                        if (!appointment.notificationSent) {
                            appointments.add(appointment.copy(id = childSnapshot.key ?: ""))
                        }
                    }
                }
                trySend(appointments)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        appointmentsRef.addValueEventListener(listener)
        awaitClose { appointmentsRef.removeEventListener(listener) }
    }

    suspend fun markNotificationSent(id: String) {
        appointmentsRef.child(id).child("notificationSent").setValue(true).await()
    }
}
