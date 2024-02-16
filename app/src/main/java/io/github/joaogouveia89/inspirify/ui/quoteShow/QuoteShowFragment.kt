package io.github.joaogouveia89.inspirify.ui.quoteShow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.github.joaogouveia89.inspirify.InspirifyApplication
import io.github.joaogouveia89.inspirify.R
import io.github.joaogouveia89.inspirify.data.DataRequest
import io.github.joaogouveia89.inspirify.databinding.FragmentQuoteShowBinding

class QuoteShowFragment : Fragment() {

    private var _binding: FragmentQuoteShowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val inspirifyApplication: InspirifyApplication by lazy {
        (requireActivity().application as InspirifyApplication)
    }

    private val viewModel: QuoteShowViewModel by viewModels {
        QuoteShowViewModelFactory(inspirifyApplication.inspirifyComponent)
    }

    private val quoteRequestStatusObserver = Observer<DataRequest>{ response ->
        when(response){
            is DataRequest.OnProgress -> {

            }
            is DataRequest.Success<*> -> {
                val quote = response.data as? Quote
                binding.quote = quote
            }
            is DataRequest.Failed ->{
                Log.i("JOAODEBUG", response.errorMessage)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentQuoteShowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.quoteRequestStatus.observe(viewLifecycleOwner, quoteRequestStatusObserver)
        viewModel.fetchRandomQuote()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}