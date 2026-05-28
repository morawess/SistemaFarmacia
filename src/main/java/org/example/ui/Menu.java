package org.example.ui;

import org.example.model.Client;
import org.example.model.Product;
import org.example.model.Sale;
import org.example.repository.ClientManageable;
import org.example.repository.ProductConsultable;
import org.example.repository.SaleProcessable;

import java.util.Scanner;

public class Menu {
    private final SaleProcessable saleProcessor;
    private final ClientManageable clientManager;
    private final ProductConsultable productConsultant;
    private final Scanner scanner;

    public Menu(SaleProcessable saleProcessor, ClientManageable clientManager, ProductConsultable productConsultant) {
        this.saleProcessor = saleProcessor;
        this.clientManager = clientManager;
        this.productConsultant = productConsultant;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int option = 0;

        while (option != 4) {
            System.out.println("SISTEMA DE MANEJO DE FARMACIA");
            System.out.println("1. Iniciar una venta");
            System.out.println("2. Ingresar cliente");
            System.out.println("3. Consultar producto");
            System.out.println("4. Salir");
            System.out.print("Elija una opción: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ingrese un número válido!");
                continue;
            }

            switch (option) {
                case 1:
                    startSale();
                    break;
                case 2:
                    registerClient();
                    break;
                case 3:
                    consultProduct();
                    break;
                case 4:
                    System.out.println("saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opcion no válida. Intente de nuevo.");
            }
        }
    }
    private void startSale() {
        System.out.println(" INICIAR NUEVA VENTA");
        System.out.print("Ingrese el DNI del cliente: ");
        String dni = scanner.nextLine();

        //se busca al cliente
        Client client = clientManager.getClientByDni(dni);

        // si es null, lo registramos
        if (client == null) {
            System.out.println("Cliente no encontrado. Vamos a registrarlo rápidamente.");
            System.out.print("Nombre del cliente: ");
            String name = scanner.nextLine();

            clientManager.saveClient(new Client(name, dni));

            // se vuelve a buscar para obtener el objeto completo con el id generado
            client = clientManager.getClientByDni(dni);
            System.out.println("Cliente registrado exitosamente.");
        } else {
            System.out.println("Cliente encontrado: " + client.getName());
        }

        // 3. se carga el producto (1 producto por venta)
        System.out.print("\nIngrese el ID del producto a vender: ");
        int productId;
        try {
            productId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Cancelando venta.");
            return;
        }

        Product product = productConsultant.getProductById(productId);

        if (product == null) {
            System.out.println("El producto no existe. Cancelando venta.");
            return;
        }

        System.out.println("Producto: " + product.getName() + " | Stock actual: " + product.getStock());
        System.out.print("Cantidad a llevar: ");
        int quantity;
        try {
            quantity = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("⚠Cantidad inválida. Cancelando venta.");
            return;
        }

        //se valida el stock
        if (quantity > product.getStock()) {
            System.out.println("No hay stock suficiente para esta venta.");
            return;
        }

        //se calcula el total
        double total = product.getPrice() * quantity;
        System.out.println("\n=============================");
        System.out.println("TOTAL A PAGAR: $" + total);
        System.out.println("=============================");

        System.out.print("¿Confirmar venta? (S/N): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("S")) {
            System.out.println("\nprocesando guardado en la base de datos...");

            // se crea el objeto Sale. el id y la fecha se generan en el sql
            Sale newSale = new Sale();
            newSale.setClientId(client.getId());
            newSale.setTotal(total);

            //se envia todo junto
            saleProcessor.processSale(newSale, productId, quantity);

        } else {
            System.out.println("Venta cancelada.");
        }
    }
    private void registerClient() {
        System.out.println("INGRESAR NUEVO CLIENTE");
        System.out.print("Nombre del cliente: ");
        String name = scanner.nextLine();

        System.out.print("DNI del cliente: ");
        String dni = scanner.nextLine();

        Client newClient = new Client(name, dni);
        clientManager.saveClient(newClient);
    }

    private void consultProduct() {
        System.out.println("CONSULTAR PRODUCTO");
        System.out.print("Ingrese el ID del producto (ej. 1 para Ibuprofeno): ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Product product = productConsultant.getProductById(id);

            if (product != null) {
                System.out.println("Producto encontrado:");
                System.out.println("- Nombre: " + product.getName());
                System.out.println("- Precio: $" + product.getPrice());
                System.out.println("- Stock disponible: " + product.getStock() + " unidades");
            } else {
                System.out.println("No se encontro ningun producto con el ID " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("El ID debe ser un número válido.");
        }
    }
}