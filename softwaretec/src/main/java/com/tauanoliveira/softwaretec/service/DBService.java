package com.tauanoliveira.softwaretec.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;

import com.tauanoliveira.softwaretec.domain.Cliente;
import com.tauanoliveira.softwaretec.domain.Endereco;
import com.tauanoliveira.softwaretec.domain.Equipamento;
import com.tauanoliveira.softwaretec.domain.Funcionario;
import com.tauanoliveira.softwaretec.domain.ItemPedido;
import com.tauanoliveira.softwaretec.domain.OrdemServico;
import com.tauanoliveira.softwaretec.domain.Pagamento;
import com.tauanoliveira.softwaretec.domain.PagamentoComBoleto;
import com.tauanoliveira.softwaretec.domain.enums.AprovaServico;
import com.tauanoliveira.softwaretec.domain.enums.EstadoPagamento;
import com.tauanoliveira.softwaretec.domain.enums.EstadoServico;
import com.tauanoliveira.softwaretec.domain.enums.TipoCliente;
import com.tauanoliveira.softwaretec.repository.ClienteRepository;
import com.tauanoliveira.softwaretec.repository.EnderecoRepository;
import com.tauanoliveira.softwaretec.repository.EquipamentoRepository;
import com.tauanoliveira.softwaretec.repository.FuncionarioRepository;
import com.tauanoliveira.softwaretec.repository.ItemPedidoRepository;
import com.tauanoliveira.softwaretec.repository.OrdemServicoRepository;
import com.tauanoliveira.softwaretec.repository.PagamentoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DBService {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void instantiateTestDatabase() throws ParseException{

        Funcionario F1 = new Funcionario(null, "funcADMIN@gmail.com", bCryptPasswordEncoder.encode("123456"), 1);
        Funcionario F2 = new Funcionario(null, "funcA@gmail.com", bCryptPasswordEncoder.encode("123456"), 2);
        Funcionario F3 = new Funcionario(null, "funcT@gmail.com", bCryptPasswordEncoder.encode("123456"), 3);
        Cliente cli1 = new Cliente(null, "Tauan Oliveira", "tauan-oliv@hotmail.com", "10290831938", "999705045", TipoCliente.PESSOAFISICA);
        Cliente cli2 = new Cliente(null, "João Silveira", "joao-ss@hotmail.com", "86300219000121", "555554445", TipoCliente.PESSOAJURIDICA);
        Endereco end1 = new Endereco(null, cli1, "Santa Catarina", "Criciúma", "Nossa senhora da salete", "55", "888120451");
        OrdemServico ord1 = new OrdemServico(null, cli1, EstadoServico.PENDENTE);
        OrdemServico ord2 = new OrdemServico(null, cli1, EstadoServico.PENDENTE);
        ItemPedido it1 = new ItemPedido(null, ord1, new BigDecimal("800.00"), new BigDecimal("10"), AprovaServico.PENDENTE);
        ItemPedido it2 = new ItemPedido(null, ord1, new BigDecimal("1500.00"), new BigDecimal("15"), AprovaServico.PENDENTE);
        ItemPedido it3 = new ItemPedido(null, ord2, new BigDecimal("1500.00"), new BigDecimal("15"), AprovaServico.PENDENTE);
        Equipamento eq1 = new Equipamento(null, it1, "Celular", "quebro");
        Equipamento eq2 = new Equipamento(null, it2, "not", "quebro Tambem");
        Equipamento eq3 = new Equipamento(null, it3, "TV", "quebro D+");
        Pagamento p1 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ord1, LocalDate.of(2021, 07, 14), null);
        Pagamento p2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ord2, LocalDate.of(2021, 07, 14), null);

        funcionarioRepository.saveAll(Arrays.asList(F1, F2, F3));
        clienteRepository.saveAll(Arrays.asList(cli1, cli2));
        enderecoRepository.saveAll(Arrays.asList(end1));

        cli1.setEndereco(end1);
        ord1.setPagamento(p1);
        ord2.setPagamento(p2);
        ordemServicoRepository.saveAll(Arrays.asList(ord1, ord2));
        pagamentoRepository.saveAll(Arrays.asList(p1, p2));

        cli1.getOrdemServicos().addAll(Arrays.asList(ord1, ord2));

        equipamentoRepository.saveAll(Arrays.asList(eq1, eq2, eq3));

        it1.setEquipamento(eq1);
        it2.setEquipamento(eq2);
        it3.setEquipamento(eq3);
        itemPedidoRepository.saveAll(Arrays.asList(it1, it2, it3));

        ord1.getItens().addAll(Arrays.asList(it1, it2));
        ord1.getItens().addAll(Arrays.asList(it3));
        equipamentoRepository.saveAll(Arrays.asList(eq1, eq2, eq3));
    }
}