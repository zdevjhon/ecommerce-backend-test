package com.ecommerce.test.service;

import com.ecommerce.test.dto.DetallePedidoRequest;
import com.ecommerce.test.model.DetallePedido;
import com.ecommerce.test.model.Pedido;
import com.ecommerce.test.repository.DetallePedidoRepository;
import com.ecommerce.test.repository.PedidoRepository;

//@ExtendWith(MockitoExtension.class) // Usamos MockitoExtension para JUnit 5
class DetallePedidoServiceTest {

    /*@Mock
    private DetallePedidoRepository detallePedidoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private DetallePedidoService detallePedidoService;

    @Test
    void saveDetallePedido_CuandoPedidoExiste_DeberiaGuardarDetalle() {
        // Arrange
        Pedido pedido = new Pedido();
        pedido.setId(1L);

        DetallePedidoRequest detalleRequest = new DetallePedidoRequest();
        detalleRequest.setPedidoId(1L);
        detalleRequest.setProductoId(9L);
        detalleRequest.setCantidad(1);
        detalleRequest.setPrecio(2.5);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // Act
        detallePedidoService.saveDetallePedido(detalleRequest);

        // Assert
        verify(detallePedidoRepository, times(1)).save(any(DetallePedido.class));
    }

    @Test
    void saveDetallePedido_CuandoPedidoNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        DetallePedidoRequest detalleRequest = new DetallePedidoRequest();
        detalleRequest.setPedidoId(1L);
        detalleRequest.setProductoId(9L);
        detalleRequest.setCantidad(1);
        detalleRequest.setPrecio(2.5);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        try {
            detallePedidoService.saveDetallePedido(detalleRequest);
        } catch (RuntimeException e) {
            // Excepción esperada
        }

        verify(detallePedidoRepository, never()).save(any(DetallePedido.class));
    }*/
}
