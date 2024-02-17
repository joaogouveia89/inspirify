package io.github.joaogouveia89.inspirify.ui.quoteShow

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
import io.github.joaogouveia89.inspirify.databinding.FragmentQuoteShowBinding

class QuoteShowFragment : Fragment() {

    private var _binding: FragmentQuoteShowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val inspirifyApplication: InspirifyApplication by lazy {
        (requireActivity().application as InspirifyApplication)
    }

    private val viewModel: QuoteShowViewModel by viewModels {
        QuoteShowViewModelFactory(inspirifyApplication.inspirifyComponent)
    }

    private val onQuoteUpdateObserver = Observer<DataRequest> { response ->
        when (response) {
            is DataRequest.OnProgress -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            is DataRequest.Success<*> -> {
                binding.progressBar.visibility = View.INVISIBLE
                val quote = response.data as? Quote
                binding.quote = quote
            }

            is DataRequest.Failed -> {
                binding.progressBar.visibility = View.INVISIBLE
                view?.let {
                    Snackbar.make(it, response.errorMessage, Snackbar.LENGTH_SHORT).apply {
                        this.setBackgroundTint(ContextCompat.getColor(context, R.color.red))
                    }.show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // I dont want to call the fetching action on fragment resuming
        viewModel.fetchRandomQuote()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentQuoteShowBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.quoteRequestStatus.observe(viewLifecycleOwner, onQuoteUpdateObserver)
        viewModel.quoteFavoriteUpdateStatus.observe(viewLifecycleOwner, onQuoteUpdateObserver)
        binding.favoriteIv.setOnClickListener {
            viewModel.inputs.onFavoriteClick.postValue(Unit)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}