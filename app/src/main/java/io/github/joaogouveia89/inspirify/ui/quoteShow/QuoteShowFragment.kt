package io.github.joaogouveia89.inspirify.ui.quoteShow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import io.github.joaogouveia89.inspirify.InspirifyApplication
import io.github.joaogouveia89.inspirify.databinding.FragmentQuoteShowBinding

class QuoteShowFragment : Fragment() {

    //TODO: Refactor it to use binding just like the favorites fragment and solve progresssbar not showing here

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

    private val onCurrentQuoteObserver = Observer<Quote> {
        binding.quote = it
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // I dont want to fetch new quote on fragment resuming
        viewModel.inputs.requestNewData.postValue(Unit)
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

        binding.swiperefresh.setOnRefreshListener {
            binding.swiperefresh.isRefreshing = false
            viewModel.inputs.requestNewData.postValue(Unit)
        }

        binding.quote = viewModel.currentQuote.value

        viewModel.currentQuote.observe(viewLifecycleOwner, onCurrentQuoteObserver)
        binding.favoriteIv.setOnClickListener {
            viewModel.inputs.onFavoriteClick.postValue(Unit)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.currentQuote.removeObservers(viewLifecycleOwner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}