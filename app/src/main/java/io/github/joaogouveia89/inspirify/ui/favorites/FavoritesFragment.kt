package io.github.joaogouveia89.inspirify.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import io.github.joaogouveia89.inspirify.InspirifyApplication
import io.github.joaogouveia89.inspirify.R
import io.github.joaogouveia89.inspirify.data.DataRequest
import io.github.joaogouveia89.inspirify.data.local.entities.Favorite
import io.github.joaogouveia89.inspirify.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val inspirifyApplication: InspirifyApplication by lazy {
        (requireActivity().application as InspirifyApplication)
    }

    private val viewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory(inspirifyApplication.inspirifyComponent)
    }

    private val adapter = FavoritesListAdapter()

    private val favoritesRequestStatusObserver = Observer<DataRequest> { response ->
        when (response) {
            is DataRequest.OnProgress -> {
                //binding.progressBar.visibility = View.VISIBLE
            }

            is DataRequest.Success<*> -> {
                // binding.progressBar.visibility = View.INVISIBLE
                val favorites = response.data as? List<Favorite>
                // binding.quote = quote
                favorites?.let {
                    adapter.submitList(it)
                    binding.favoritesListRv.visibility = View.VISIBLE
                    binding.noFavoritesTv.visibility = View.GONE
                }
            }

            is DataRequest.Failed -> {
                //binding.progressBar.visibility = View.INVISIBLE
                view?.let {
                    Snackbar.make(it, response.errorMessage, Snackbar.LENGTH_SHORT).apply {
                        this.setBackgroundTint(ContextCompat.getColor(context, R.color.red))
                    }.show()
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favoritesListRv.adapter = adapter

        viewModel.inputs.requestNewData.postValue(Unit)
        viewModel.outputs.favoritesRequestStatus.observe(
            viewLifecycleOwner,
            favoritesRequestStatusObserver
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}