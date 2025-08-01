package com.recommendations.core.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents data related to Solr indexed information.
 * This class encapsulates the id and score associated with a Solr indexed product.
 */
public class SolrIndexedData implements Serializable {

    private String id;
    private long score;

    /**
     * Default constructor.
     */
    public SolrIndexedData() {
    }

    /**
     * Constructor to initialize the SolrIndexedData object with id and score.
     *
     * @param id    the product's id (e.g., product code)
     * @param score the product's score (e.g., sales rank, relevance score)
     */
    public SolrIndexedData(String id, long score) {
        this.id = id;
        this.score = score;
    }

    /**
     * Gets the product ID.
     *
     * @return the product's id (e.g., product code)
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the product ID.
     *
     * @param id the product's id (e.g., product code)
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the score associated with the product.
     *
     * @return the product's score (e.g., sales rank, relevance score)
     */
    public long getScore() {
        return score;
    }

    /**
     * Sets the score associated with the product.
     *
     * @param score the product's score
     */
    public void setScore(long score) {
        this.score = score;
    }

    /**
     * Generates a string representation of the SolrIndexedData object.
     *
     * @return a string representation of the object in the form "id: <id>, score: <score>"
     */
    @Override
    public String toString() {
        return "SolrIndexedData{" +
                "id='" + id + '\'' +
                ", score=" + score +
                '}';
    }

    /**
     * Compares this object to another SolrIndexedData object for equality.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolrIndexedData that = (SolrIndexedData) o;
        return score == that.score && Objects.equals(id, that.id);
    }

    /**
     * Generates a hash code for the SolrIndexedData object.
     *
     * @return a hash code value for the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, score);
    }
}
