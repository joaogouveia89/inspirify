package io.github.joaogouveia89.inspirify.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.joaogouveia89.inspirify.InspirifyApplication
import io.github.joaogouveia89.inspirify.R
import io.github.joaogouveia89.inspirify.data.local.entities.Favorite
import io.github.joaogouveia89.inspirify.databinding.FragmentFavoritesBinding
import io.github.joaogouveia89.inspirify.uiHelpers.recyclerView.SwipeToDeleteCallback

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val inspirifyApplication: InspirifyApplication by lazy {
        (requireActivity().application as InspirifyApplication)
    }

    private val favoritesViewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory(inspirifyApplication.inspirifyComponent)
    }

    private val adapter = FavoritesListAdapter()

    private var itemTouchHelper: ItemTouchHelper? = null

    /*
    * Initializes the swipe-to-delete callback lazily. This ensures that the callback is
    * created only when it's accessed for the first time. Since the SwipeToDeleteCallback
    * requires a valid context, using lazy ensures that it's initialized after the fragment
    * is attached to a context. This avoids IllegalStateExceptions related to accessing
    * requireContext() before the fragment is attached. The callback is created as an
    * anonymous object, overriding the necessary methods to handle swipe actions in the
    * RecyclerView.
    */
    private val swipeToDeleteCallback: SwipeToDeleteCallback by lazy {
        object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val favorite = adapter.currentList[position]

                // dialog for confirmation
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.confirm_delete))
                    .setMessage(
                        resources.getString(
                            R.string.favorite_delete_message,
                            favorite.author
                        )
                    )
                    .setNegativeButton(resources.getString(R.string.cancel)) { _, _ ->
                        // Respond to negative button press
                        adapter.notifyItemChanged(position)
                    }
                    .setPositiveButton(resources.getString(R.string.delete)) { _, _ ->
                        // Respond to positive button press
                        favoritesViewModel.inputs.onFavoriteDelete.postValue(favorite.id)
                        adapter.notifyItemChanged(position)
                    }
                    .show()

                // Handle the swipe action
                // Update your dataset (e.g., remove the item from the list)
            }
        }
    }

    private val currentFavoritesListObserver = Observer<List<Favorite>> {
        adapter.submitList(it)
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

        binding.apply {
            lifecycleOwner = this@FavoritesFragment
            favoritesAdapter = adapter
            viewModel = favoritesViewModel
        }

        favoritesViewModel.currentFavoritesList.observe(
            viewLifecycleOwner,
            currentFavoritesListObserver
        )

        if (itemTouchHelper == null) {
            itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        }

        itemTouchHelper?.attachToRecyclerView(binding.favoritesListRv)

        favoritesViewModel.inputs.requestNewData.postValue(Unit)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}