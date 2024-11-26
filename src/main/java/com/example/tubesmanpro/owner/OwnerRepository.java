package com.example.tubesmanpro.owner;

import java.util.List;

public interface OwnerRepository {
    List<Owner> showAllOwner();
    List<Owner> findOwner (String username, String password);
}
