package io.github.joaogouveia89.inspirify.ui.quoteShow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.github.joaogouveia89.inspirify.R
import io.github.joaogouveia89.inspirify.databinding.FragmentQuoteShowBinding

class QuoteShowFragment : Fragment() {

    private var _binding: FragmentQuoteShowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: QuoteShowViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(QuoteShowViewModel::class.java)

        _binding = FragmentQuoteShowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onNewQuoteAvailable.value?.let {
            binding.quoteTv.text = it.quote
            binding.authorTv.text = it.author
            val icon = if(it.isFavorite) R.drawable.ic_like_fill else R.drawable.ic_like
            binding.favoriteIv.setImageDrawable(ResourcesCompat.getDrawable(
                resources,
                icon,
                context?.theme
            ))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}