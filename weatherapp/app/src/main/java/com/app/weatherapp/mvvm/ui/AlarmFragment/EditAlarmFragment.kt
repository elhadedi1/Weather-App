package com.app.weatherapp.mvvm.ui.AlarmFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.app.weatherapp.R
import com.app.weatherapp.databinding.FragmentEditAlarmBinding
import com.app.weatherapp.mvvm.data.model.Settings
import kotlinx.android.synthetic.main.fragment_edit_alarm.*


class EditAlarmFragment : Fragment() {
    private lateinit var binding:FragmentEditAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_alarm, container, false)

        binding.confirmButton.setOnClickListener {



            Settings.minutes=(time_picker.minute).toString()
            Settings.hours=(time_picker.hour).toString()

            Navigation.findNavController(it!!).navigate(R.id.action_editAlarmFragment_to_alarmFragment)
            Toast.makeText(requireActivity(), "You Add Alarm At "+Settings.hours +":" +Settings.minutes, Toast.LENGTH_LONG).show()



        }
        return binding.root
    }

    companion object {

        fun newInstance() =
            EditAlarmFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}