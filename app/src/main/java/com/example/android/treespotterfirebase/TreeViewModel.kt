package com.example.android.treespotterfirebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

private const val TAG = "TREE_VIEW_MODEL"
class TreeViewModel: ViewModel() {

    private val db = Firebase.firestore
    private val treeCollectionReference = db.collection("trees")

    val latestTress = MutableLiveData<List<Tree>>()

    private val latestTreeListener = treeCollectionReference
        .orderBy("dateSpotted", Query.Direction.DESCENDING)
        .limit(10)
        .addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error fetching latest trees", error)
            }
            else if (snapshot != null) {
                // val trees = snapshot.toObjects(Tree::class.java)
                val trees = mutableListOf<Tree>()
                for (treeDocument in snapshot) {
                    val tree = treeDocument.toObject(Tree::class.java)
                    tree.documentReference = treeDocument.reference
                    trees.add(tree)
                }
                Log.d(TAG, "Trees from firebase: $trees")
                latestTress.postValue(trees)
            }
        }

    fun setIsFavorite(tree: Tree, favorite: Boolean) {
        Log.d(TAG, "Updating tree ${tree} to favorite ${favorite}")
        tree.documentReference?.update("favorite", favorite)
    }

    fun addTree(tree: Tree) {
        treeCollectionReference.add(tree)
            .addOnSuccessListener { treeDocumentReferencce ->
                Log.d(TAG, "New tree added at ${treeDocumentReferencce.path}")
            }
            .addOnFailureListener {error ->
                Log.e(TAG, "Error adding tree $tree", error)
            }
    }

    fun deleteTree(tree: Tree) {
        tree.documentReference?.delete()
    }
}