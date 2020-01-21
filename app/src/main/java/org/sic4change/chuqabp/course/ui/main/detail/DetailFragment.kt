package org.sic4change.chuqabp.course.ui.main.detail

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import org.sic4change.chuqabp.R
import org.sic4change.chuqabp.course.data.database.RoomDataSource
import org.sic4change.chuqabp.course.data.server.FirebaseDataSource
import org.sic4change.chuqabp.course.ui.common.app
import org.sic4change.chuqabp.course.ui.common.bindingInflate
import org.sic4change.chuqabp.course.ui.common.getViewModel
import org.sic4change.chuqabp.databinding.FragmentDetailBinding
import org.sic4change.usescases.DeleteCase
import org.sic4change.usescases.FindCaseById

class DetailFragment: Fragment() {

    private lateinit var viewModel : DetailViewModel

    private var binding: FragmentDetailBinding? = null

    private lateinit var navController: NavController

    private val args: DetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = container?.bindingInflate(R.layout.fragment_detail, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()
        val casesRepository = org.sic4change.data.repository.CasesRepository(RoomDataSource(app.db), FirebaseDataSource())
        viewModel= getViewModel {
            DetailViewModel(args.id, FindCaseById(casesRepository), DeleteCase(casesRepository))
        }

        binding?.apply {
            viewmodel = viewModel
            lifecycleOwner = this@DetailFragment
        }

        viewModel.deleted.observe(this, Observer<Boolean> {
            if (it) {
                finish()
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_add).setVisible(false)
        menu.findItem(R.id.action_edit).setVisible(true)
        menu.findItem(R.id.action_delete).setVisible(true)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                showDeleteConfirmationDialog()
                true
            }
            R.id.action_edit -> {
                val action = DetailFragmentDirections.actionDetailFragmentToUpdateCaseFragment(args.id)
                navController.navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.delete_question_title)
        builder.setMessage(R.string.delete_question_description)
        builder.setCancelable(true)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            viewModel.deleteCase()
        }

        builder.show()
    }

    private fun finish() {
        navController.navigate(R.id.action_detailFragment_to_loginActivity)
        activity?.finish()
    }


}