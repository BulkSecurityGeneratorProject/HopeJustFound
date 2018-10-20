package org.huejacksonfoundation.hopejustfound.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Donation.
 */
@Entity
@Table(name = "donation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Donation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "initial_date")
    private LocalDate initialDate;

    @Column(name = "expire_date")
    private LocalDate expireDate;

    @OneToOne    @JoinColumn(unique = true)
    private Skilled skilled;

    @OneToOne    @JoinColumn(unique = true)
    private Unskilled unskilled;

    @OneToOne    @JoinColumn(unique = true)
    private Goods goods;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Donation type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getInitialDate() {
        return initialDate;
    }

    public Donation initialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
        return this;
    }

    public void setInitialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public Donation expireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
        return this;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public Skilled getSkilled() {
        return skilled;
    }

    public Donation skilled(Skilled skilled) {
        this.skilled = skilled;
        return this;
    }

    public void setSkilled(Skilled skilled) {
        this.skilled = skilled;
    }

    public Unskilled getUnskilled() {
        return unskilled;
    }

    public Donation unskilled(Unskilled unskilled) {
        this.unskilled = unskilled;
        return this;
    }

    public void setUnskilled(Unskilled unskilled) {
        this.unskilled = unskilled;
    }

    public Goods getGoods() {
        return goods;
    }

    public Donation goods(Goods goods) {
        this.goods = goods;
        return this;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public User getUser() {
        return user;
    }

    public Donation user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Donation donation = (Donation) o;
        if (donation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), donation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Donation{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", initialDate='" + getInitialDate() + "'" +
            ", expireDate='" + getExpireDate() + "'" +
            "}";
    }
}
