
package br.edu.fa7.rtree.service;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.edu.fa7.rtree.entity.Ponto;


@Repository
@Transactional
public class PontoService extends BaseService<Integer, Ponto>{

}
