package org.huejacksonfoundation.hopejustfound.repository;

import org.huejacksonfoundation.hopejustfound.domain.Goods;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Goods entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {

}
