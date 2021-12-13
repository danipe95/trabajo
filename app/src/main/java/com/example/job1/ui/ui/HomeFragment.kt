package com.example.job1.ui.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.job1.databinding.FragmentHomeBinding
import com.google.firebase.firestore.*
import android.R.attr.category
import android.content.Context
import androidx.navigation.Navigation
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.preference.PreferenceManager
import android.content.SharedPreferences
import androidx.lifecycle.lifecycleScope
import com.example.job1.*
import com.example.job1.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.text.DecimalFormat
import kotlin.math.roundToInt
import kotlin.math.roundToLong

public class HomeFragment : Fragment(), SearchView.OnQueryTextListener,
    AdapterView.OnItemSelectedListener, ProductAdapter.OnItemClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recycleView: RecyclerView
    private var db = FirebaseFirestore.getInstance()
    private var listProduct = mutableListOf<ProductEntity>()
    private lateinit var productAdapter: ProductAdapter

    private lateinit var svSearch: SearchView

    private lateinit var spinnerCategory: Spinner
    private lateinit var spinnerSeller: Spinner
    private var listCategory = arrayListOf<String>()
    private var listSeller = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Recycler
        recycleView = root.findViewById<RecyclerView>(R.id.reclycler)
        recycleView.layoutManager = LinearLayoutManager(activity)
        recycleView.setHasFixedSize(true)

        getAllProduct()
        productAdapter = ProductAdapter(listProduct, this);
        recycleView.adapter = productAdapter;

        //Search
        svSearch = root.findViewById<SearchView>(R.id.svSearch)
        svSearch.setOnQueryTextListener(this)

        //Spinner Category
        spinnerCategory = root.findViewById(R.id.category_spinner)
        spinnerCategory.onItemSelectedListener = this
        listCategory.add(resources.getString(R.string.test_All))

        activity?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                listCategory
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCategory.adapter = adapter
            }
        }

        //Spinner Seller
        spinnerSeller = root.findViewById(R.id.seller_spinner)
        spinnerSeller.onItemSelectedListener = this
        listSeller.add(resources.getString(R.string.test_All))

        activity?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                listSeller
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerSeller.adapter = adapter
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getAllProduct() {
        listProduct.clear()

        db.collection("product").get().addOnSuccessListener { result ->
            for (document in result) {

                if (!listCategory.contains(document.data["category"].toString())) {
                    listCategory.add(document.data["category"].toString())
                }

                if (!listSeller.contains(document.data["seller"].toString())) {
                    listSeller.add(document.data["seller"].toString())
                }

                var productExist = listProduct.find { it.id == document.id }

                if (productExist == null) {
                    listProduct.add(
                        ProductEntity(
                            document.data["imagen"].toString(),
                            document.data["title"].toString(),
                            document.data["cost"].toString(),
                            document.data["category"].toString(),
                            document.data["seller"].toString(),
                            document.id,
                            document.data["average"].toString().toDouble()
                        )
                    )
                }

            }

            lifecycleScope.launch {
                averageScore()
            }

            productAdapter.notifyDataSetChanged();
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true;
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText!!.isEmpty()) {
            getAllProduct()
        } else {
            searchForTitle(newText)
        }
        return true;
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        if (parent != null && parent.getItemAtPosition(position).toString().isNotEmpty()) {

            var category: String = spinnerCategory.getSelectedItem().toString()
            var seller: String = spinnerSeller.getSelectedItem().toString()

            var all = resources.getString(R.string.test_All)

            if (category == all && seller == all) {
                getAllProduct()
            } else {
                if (category == all) {
                    filterForSeller(seller)
                } else {
                    if (seller == all) {
                        filterForCategory(category)
                    } else {
                        filterForCategoryAndSeller(category, seller)
                    }
                }
            }

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun filterForCategory(category: String) {
        listProduct.clear()

        db.collection("product")
            .whereEqualTo("category", category)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    var productExist = listProduct.find { it.id == document.id }

                    if (productExist == null) {
                        listProduct.add(
                            ProductEntity(
                                document.data["imagen"].toString(),
                                document.data["title"].toString(),
                                document.data["cost"].toString(),
                                document.data["category"].toString(),
                                document.data["seller"].toString(),
                                document.id,
                                document.data["average"].toString().toDouble()
                            )
                        )
                    }
                }
                productAdapter.notifyDataSetChanged();
            }
    }

    private fun filterForSeller(seller: String) {
        listProduct.clear()

        db.collection("product")
            .whereEqualTo("seller", seller)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    var productExist = listProduct.find { it.id == document.id }

                    if (productExist == null) {
                        listProduct.add(
                            ProductEntity(
                                document.data["imagen"].toString(),
                                document.data["title"].toString(),
                                document.data["cost"].toString(),
                                document.data["category"].toString(),
                                document.data["seller"].toString(),
                                document.id,
                                document.data["average"].toString().toDouble()
                            )
                        )
                    }
                }
                productAdapter.notifyDataSetChanged();
            }

    }

    private fun filterForCategoryAndSeller(category: String, seller: String) {
        listProduct.clear()

        db.collection("product")
            .whereEqualTo("seller", seller).whereEqualTo("category", category)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    var productExist = listProduct.find { it.id == document.id }

                    if (productExist == null) {
                        listProduct.add(
                            ProductEntity(
                                document.data["imagen"].toString(),
                                document.data["title"].toString(),
                                document.data["cost"].toString(),
                                document.data["category"].toString(),
                                document.data["seller"].toString(),
                                document.id,
                                document.data["average"].toString().toDouble()
                            )
                        )
                    }
                }

                productAdapter.notifyDataSetChanged();
            }

    }

    private fun searchForTitle(newText: String) {
        listProduct.clear()
        db.collection("product")
            .whereGreaterThanOrEqualTo("title", newText)
            .whereLessThanOrEqualTo("title", (newText + "\uF7FF"))
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    var productExist = listProduct.find { it.id == document.id }

                    if (productExist == null) {
                        listProduct.add(
                            ProductEntity(
                                document.data["imagen"].toString(),
                                document.data["title"].toString(),
                                document.data["cost"].toString(),
                                document.data["category"].toString(),
                                document.data["seller"].toString(),
                                document.id,
                                document.data["average"].toString().toDouble()
                            )
                        )
                    }
                }

                productAdapter.notifyDataSetChanged();
            }

    }

    override fun onItemClick(position: Int) {

        val productItem: ProductEntity = listProduct[position]

        //Go  ProductActivity
        var bundle = Bundle()
        bundle.putString("product", productItem.id)
        parentFragmentManager.setFragmentResult("key", bundle)



    }


    private suspend fun averageScore() {

        for (product in listProduct) {
            var average = 0.0
            db.collection("product").document(product.id).collection("comments").get()
                .addOnSuccessListener {

                    if (it.any()) {

                        for (score in it) {
                            average += score.data["score"].toString().toDouble()
                        }

                        db.collection("product").document(product.id).update(
                            "average",  average / it.count()
                        )
                    }
                }.await()
        }
    }


}