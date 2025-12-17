package com.example.bai2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.bai2.R
import com.example.bai2.data.entity.Appointment
import com.example.bai2.databinding.ItemAppointmentBinding
import java.time.format.DateTimeFormatter

class AppointmentAdapter(
    private var appointments: List<Appointment> = emptyList(),
    private val onItemClick: (Appointment) -> Unit
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    fun updateAppointments(newAppointments: List<Appointment>) {
        this.appointments = newAppointments
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.bind(appointments[position])
    }

    override fun getItemCount() = appointments.size

    class AppointmentViewHolder(
        private val binding: ItemAppointmentBinding,
        private val onItemClick: (Appointment) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(appointment: Appointment) {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

            binding.apply {
                appointmentNameTextView.text = appointment.name
                personNameTextView.text = "Người hẹn: ${appointment.personName}"
                descriptionTextView.text = appointment.description
                locationTextView.text = appointment.location
                fromTimeTextView.text = "Từ: ${appointment.getFromTimeAsLocalDateTime().format(formatter)}"
                toTimeTextView.text = "Đến: ${appointment.getToTimeAsLocalDateTime().format(formatter)}"

                // Load avatar using Coil
                avatarImageView.load(appointment.personAvatarUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_foreground)
                    error(R.drawable.ic_launcher_foreground)
                }

                root.setOnClickListener { onItemClick(appointment) }
            }
        }
    }
}
