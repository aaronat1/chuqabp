package org.sic4change.chuqabp.course.ui.main.newcase

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_main.bttNavigation
import kotlinx.android.synthetic.main.fragment_new_case.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel
import org.sic4change.chuqabp.R
import org.sic4change.chuqabp.course.ui.common.bindingInflate
import org.sic4change.chuqabp.course.ui.main.main.PersonsAdapter
import org.sic4change.chuqabp.databinding.FragmentNewCaseBinding
import org.sic4change.domain.Person
import java.util.*

class NewCaseFragment: Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var adapter : PersonsAdapter

    private lateinit var datePickerDialog: DatePickerDialog

    private lateinit var timePickerDialog: TimePickerDialog

    private var binding: FragmentNewCaseBinding? = null

    private lateinit var navController: NavController


    private val viewModel : NewCaseViewModel by currentScope.viewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = container?.bindingInflate(R.layout.fragment_new_case, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.actionBar?.setDisplayHomeAsUpEnabled(false)
        navController = view.findNavController()

        adapter = PersonsAdapter(viewModel::onPersonClicked)
        recycler_persons_selector.adapter = adapter

        binding?.apply {
            viewmodel = viewModel
            lifecycleOwner = this@NewCaseFragment
        }

        bttNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.new_case -> {
                    true
                }
                R.id.cases -> {
                    navController.navigate(R.id.action_newCaseFragment_to_casesFragement)
                    true
                }
                R.id.persons -> {
                    navController.navigate(R.id.action_newCaseFragment_to_mainFragment)
                    true
                }
                R.id.training -> {
                    navController.navigate(R.id.action_newCaseFragment_to_trainingFragment)
                    true
                }
                R.id.configuration -> {
                    navController.navigate(R.id.action_newCaseFragment_to_userFragment)
                    true
                }
                else -> false
            }
        }

        cvPerson.setOnClickListener {
            hideShowPersonSelection()
        }

        cvDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            datePickerDialog = DatePickerDialog(this.context!!, R.style.DateTimeDialogTheme,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val month = monthOfYear + 1
                    viewModel.setDate("$dayOfMonth/$month/$year")
                tvDate.text = "$dayOfMonth/$month/$year"
            }, year, month, day)

            datePickerDialog.show()

        }

        cvTime.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            timePickerDialog = TimePickerDialog(this.context!!, R.style.DateTimeDialogTheme,
                TimePickerDialog.OnTimeSetListener { view, hour, minute  ->
                    viewModel.setHour("$hour:$minute")
                tvTime.text = "$hour:$minute"
            }, hour, minute, true)

            timePickerDialog.show()

        }

        cvPysical.setOnCheckedChangeListener { compoundButton, b ->
            setType()
        }

        cvSexual.setOnCheckedChangeListener { compoundButton, b ->
            setType()
        }

        cvPsychological.setOnCheckedChangeListener { compoundButton, b ->
            setType()
        }

        cvSocial.setOnCheckedChangeListener { compoundButton, b ->
            setType()
        }

        cvEconomic.setOnCheckedChangeListener { compoundButton, b ->
            setType()
        }

        etHow.addTextChangedListener {
            if (it != null && it.isNotEmpty()) {
                enabledRegister()
                ivSixStep.setImageResource(R.drawable.ic_check)
            } else {
                disabledRegister()
                ivSixStep.setImageResource(R.drawable.ic_looks_6)
            }
        }

        btnRegisterCase.setOnClickListener {
            viewModel.onRegisterCaseClicked(viewModel.person.value?.id, tvDate.text.toString(),
                tvTime.text.toString(), tvPlace.text.toString(), cvPysical.isChecked, cvSexual.isChecked,
                cvPsychological.isChecked, cvSocial.isChecked, cvEconomic.isChecked, etHow.text.toString())
        }

        viewModel.person.observe(this, Observer<Person> {
            hideShowPersonSelection()
            tvPersonName.text = "${it.name} ${it.surnames}"
            ivOneStep.setImageResource(R.drawable.ic_check)
            enabledDayQuestion()
        })

        viewModel.date.observe(this, Observer<String> {
            ivTwoStep.setImageResource(R.drawable.ic_check)
            enabledHourQuestion()
        })

        viewModel.hour.observe(this, Observer<String> {
            ivThreeStep.setImageResource(R.drawable.ic_check)
            enabledPlaceQuestion()
            if (!it.contains("null") && cvPlace.visibility == VISIBLE) {
                ivFourStep.setImageResource(R.drawable.ic_check)
                enabledTypeQuestion()
            }
        })

        viewModel.currentLocation.observe(this, Observer<String> {
            etPlace.setText(it)
        })

        viewModel.type.observe(this, Observer<Boolean> {
            if (it) {
                enabledHowQuestion()
                ivFiveStep.setImageResource(R.drawable.ic_check)
            } else {
                disabledHowQuestion()
                disabledRegister()
                ivFiveStep.setImageResource(R.drawable.ic_looks_5)
            }
        })

    }

    private fun setType() {
        viewModel.setType(cvPysical.isChecked || cvSexual.isChecked || cvPsychological.isChecked ||
                cvSocial.isChecked || cvEconomic.isChecked)

    }

    private fun hideShowPersonSelection() = if (recycler_persons_selector.isVisible) {
        recycler_persons_selector.visibility = GONE
        tvPersonName.text = "${viewModel.person.value?.name} ${viewModel.person.value?.surnames}"
    } else {
        recycler_persons_selector.visibility = VISIBLE
        tvPersonName.text = ""
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_refresh).isVisible = false
        menu.findItem(R.id.action_add).isVisible = false
        menu.findItem(R.id.action_account).isVisible = false
        menu.findItem(R.id.action_edit).isVisible = false
        menu.findItem(R.id.action_delete).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun onResume() {
        super.onResume()
        bttNavigation.menu.findItem(R.id.new_case).isChecked = true
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {

    }

    private fun enabledDayQuestion() {
        cvDate.visibility = VISIBLE
    }

    private fun enabledHourQuestion() {
        cvTime.visibility = VISIBLE
    }

    private fun enabledPlaceQuestion() {
        cvPlace.visibility = VISIBLE
    }

    private fun enabledTypeQuestion() {
        cvType.visibility = VISIBLE
    }

    private fun enabledHowQuestion() {
        cvHow.visibility = VISIBLE
    }

    private fun disabledHowQuestion() {
        cvHow.visibility = GONE
    }

    private fun enabledRegister() {
        btnRegisterCase.visibility = VISIBLE
    }

    private fun disabledRegister() {
        btnRegisterCase.visibility = GONE
    }


}