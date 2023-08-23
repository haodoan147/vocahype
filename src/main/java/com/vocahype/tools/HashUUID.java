package com.vocahype.tools;

import java.util.List;

public class HashUUID {
    static int numShards = 10; // Number of shards

    public static int hashUserId(String userId) {
        // Convert the UUID string to a numeric value
        long numericValue = Math.abs(userId.hashCode());

        // Calculate shard index based on numeric value
        int shardIndex = (int) (numericValue % numShards);

        return shardIndex;
    }

    public static void main(String[] args) {
        String userId = "PKyzrPUByHaugQGabfH2TEfpfft2";
        List<String> userIds = List.of("PKyzrPUByHaugQGabfH2TEfpfft2", "H6Tr8sBNjZQnpvMmn90ISt6ZzMH3",
                "H9ePRXQgB3ThU0SgO9EBY0zFoZl2", "Js8O20gFCPZhY8iCuliun13uZpF3", "CgcSVVQ4oNfja1CvkeeNfAd9MOz1");

        userIds.forEach(id -> {
            int shardIndex = hashUserId(id);
            System.out.println("User ID " + id + " belongs to shard " + shardIndex);
        });
//        int shardIndex = hashUserId(userId);
//        System.out.println("User ID " + userId + " belongs to shard " + shardIndex);
    }
}
