package com.example.myapplication;

import android.util.Log;

public class WeightBST {

    private class TreeNode {
        WeightEntry entry;
        TreeNode left;
        TreeNode right;

        TreeNode(WeightEntry entry) {
            this.entry = entry;
        }
    }

    private TreeNode root;

    public void insert(WeightEntry entry) {
        root = insertRecursive(root, entry);
    }

    private TreeNode insertRecursive(TreeNode node, WeightEntry entry) {
        if (node == null) {
            Log.d("BST", "Inserted: " + entry.getWeight());
            return new TreeNode(entry);
        }
        if (entry.getWeight() < node.entry.getWeight()) {
            node.left = insertRecursive(node.left, entry);
        } else {
            node.right = insertRecursive(node.right, entry);
        }
        return node;
    }

    public WeightEntry search(float targetWeight) {
        return searchRecursive(root, targetWeight);
    }

    private WeightEntry searchRecursive(TreeNode node, float target) {
        if (node == null) return null;

        if (node.entry.getWeight() == target) {
            return node.entry;
        } else if (target < node.entry.getWeight()) {
            return searchRecursive(node.left, target);
        } else {
            return searchRecursive(node.right, target);
        }
    }

    public WeightEntry findClosest(float targetWeight) {
        return findClosestRecursive(root, targetWeight, null);
    }

    private WeightEntry findClosestRecursive(TreeNode node, float target, WeightEntry closest) {
        if (node == null) return closest;

        Log.d("BST", "Visiting: " + node.entry.getWeight());

        if (closest == null || Math.abs(node.entry.getWeight() - target) < Math.abs(closest.getWeight() - target)) {
            closest = node.entry;
        }

        if (target < node.entry.getWeight()) {
            return findClosestRecursive(node.left, target, closest);
        } else {
            return findClosestRecursive(node.right, target, closest);
        }
    }
}
