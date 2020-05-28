package com.algamoney.api.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.algamoney.api.model.Lancamento;
import com.algamoney.api.model.Lancamento_;
import com.algamoney.api.repository.filter.LancamentoFilter;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<Lancamento> filtrar(LancamentoFilter filter, Pageable pageable) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteriaQuery = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		Predicate[] predicates = this.criarRestricoes(filter, builder, root);
		criteriaQuery.where(predicates);
		TypedQuery<Lancamento> query = this.entityManager.createQuery(criteriaQuery);
		this.adicionarRestricoesDePaginacao(query, pageable);
		return new PageImpl<>(query.getResultList(), pageable, this.total(filter));
	}

	private Long total(LancamentoFilter filter) {
		// TODO Auto-generated method stub
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		Predicate[] predicates = this.criarRestricoes(filter, builder, root);
		criteriaQuery.where(predicates);
		criteriaQuery.select(builder.count(root));
		return this.entityManager.createQuery(criteriaQuery).getSingleResult();
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<Lancamento> query, Pageable pageable) {
		// TODO Auto-generated method stub
		int paginaAtual = pageable.getPageNumber(), totalRegistrosPorPagina = pageable.getPageSize(),
				primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}

	private Predicate[] criarRestricoes(LancamentoFilter filter, CriteriaBuilder builder, Root<Lancamento> root) {
		// TODO Auto-generated method stub
		List<Predicate> list = new ArrayList<>();
		if (!StringUtils.isEmpty(filter.getDescricao())) {
			list.add(builder.like(builder.lower(root.get(Lancamento_.descricao)),
					"%" + filter.getDescricao().toLowerCase() + "%"));
		}
		if (StringUtils.isEmpty(filter.getDataVencimentoDe())) {
			list.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), filter.getDataVencimentoDe()));
		}
		if (StringUtils.isEmpty(filter.getDataVencimentoAte())) {
			list.add(builder.lessThanOrEqualTo(root.get(Lancamento_.dataPagamento), filter.getDataVencimentoAte()));
		}
		return list.toArray(new Predicate[list.size()]);
	}

}
