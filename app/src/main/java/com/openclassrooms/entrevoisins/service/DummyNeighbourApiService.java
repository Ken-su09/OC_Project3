package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.ArrayList;
import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyNeighbourApiService implements NeighbourApiService {

    private final List<Neighbour> neighbours = DummyNeighbourGenerator.generateNeighbours();
    private final List<Neighbour> favoritesNeighbours = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getNeighbours() {
        return neighbours;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getFavoritesNeighbours() {
        return favoritesNeighbours;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void neighbourChangeFavorites(long id) {
        for (int i = 0; i < neighbours.size(); i++) {
            if (neighbours.get(i).getId() == id) {
                neighbours.get(i).setIsFavorite(!favoritesNeighbours.contains(neighbours.get(i)));
                if (favoritesNeighbours.contains(neighbours.get(i))) {
                    favoritesNeighbours.remove(neighbours.get(i));
                } else {
                    favoritesNeighbours.add(neighbours.get(i));
                }
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNeighbour(Neighbour neighbour) {
        if (favoritesNeighbours.contains(neighbour)) {
            favoritesNeighbours.remove(neighbour);
        }
        neighbours.remove(neighbour);
    }

    /**
     * {@inheritDoc}
     *
     * @param neighbour
     */
    @Override
    public void createNeighbour(Neighbour neighbour) {
        neighbours.add(neighbour);
    }
}
