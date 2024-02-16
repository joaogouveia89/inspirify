package io.github.joaogouveia89.inspirify.ui.quoteShow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.github.joaogouveia89.inspirify.databinding.FragmentQuoteShowBinding

class QuoteShowFragment : Fragment() {

    private var _binding: FragmentQuoteShowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val quoteShowViewModel =
            ViewModelProvider(this).get(QuoteShowViewModel::class.java)

        _binding = FragmentQuoteShowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}