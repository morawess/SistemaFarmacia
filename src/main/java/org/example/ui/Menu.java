package org.example.ui;

import org.example.model.Client;
import org.example.model.Product;
import org.example.model.Sale;
import org.example.model.SaleDetail;
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
            System.out.println("\nSistema de Farmacity");
            System.out.println("1. Iniciar una venta");
            System.out.println("2. Ingresar cliente");
            System.out.println("3. Consultar producto");
            System.out.println("4. Salir");
            System.out.print("Elija una opcion: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ingrese un número valido");
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
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opcion no valida. Intente de nuevo.");
            }
        }
    }

    private void startSale() {
        System.out.println("\n=== INICIAR NUEVA VENTA ===");

        Sale currentSale = new Sale();

        System.out.print("¿El cliente tiene 'Mi Farmacity'? Ingrese DNI (o presione Enter para omitir): ");
        String dni = scanner.nextLine().trim();

        if (!dni.isEmpty()) {
            Client client = clientManager.getClientByDni(dni);

            if (client == null) {
                System.out.print("Cliente no encontrado. ¿Desea registrarlo ahora? (S/N): ");
                String register = scanner.nextLine();
                if (register.equalsIgnoreCase("S")) {
                    System.out.print("Nombre del cliente: ");
                    String name = scanner.nextLine();
                    clientManager.saveClient(new Client(name, dni));
                    client = clientManager.getClientByDni(dni);
                    System.out.println("Cliente registrado exitosamente.");
                }
            } else {
                System.out.println("Cliente encontrado: " + client.getName());
            }
            currentSale.setClient(client);
        } else {
            System.out.println("Venta a Consumidor Final (Sin descuentos de club).");
        }

        boolean addingProducts = true;

        while (addingProducts) {
            System.out.print("\nIngrese el ID del producto (o '0' para terminar y cobrar): ");
            String input = scanner.nextLine();

            if (input.equals("0")) {
                break; // Sale del ciclo para ir a cobrar
            }

            int productId;
            try {
                productId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("ID inválido.");
                continue;
            }

            Product product = productConsultant.getProductById(productId);

            if (product == null) {System.out.println("El producto no existe. Intente con otro ID.");
                continue;
            }

            System.out.println("Escaneado: " + product.getName() + " | Precio: $" + product.getPrice() + " | Stock: " + product.getStock());
            System.out.print("Cantidad a llevar: ");

            int quantity;
            try {
                quantity = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠ Cantidad inválida.");
                continue;
            }

            if (quantity <= 0) {
                System.out.println("La cantidad debe ser mayor a 0.");
                continue;
            }

            if (quantity > product.getStock()) {
                System.out.println("No hay stock suficiente para esta venta.");
                continue;
            }

            // ¡Acá está la magia! Le pasamos el producto a la venta.
            // La venta internamente crea el SaleDetail y calcula si hay descuento.
            currentSale.addProductToSale(product, quantity);
            System.out.println("Producto agregado al ticket.");
        }

        // 4. Confirmación y Ticket Final
        if (currentSale.getDetails().isEmpty()) {
            System.out.println("No se agregaron productos. Venta cancelada.");
            return;
        }

        System.out.println("\n========================================");
        System.out.println("             TICKET DE VENTA            ");
        System.out.println("========================================");

        for (SaleDetail detail : currentSale.getDetails()) {
            System.out.printf("- %s (x%d)\n", detail.getProduct().getName(), detail.getQuantity());
            System.out.printf("  Subtotal: $%.2f | Descuento: $%.2f | A Pagar: $%.2f\n",
                    detail.getSubtotal(), detail.getDiscountAmount(), detail.getFinalTotal());
        }

        System.out.println("----------------------------------------");
        System.out.printf("TOTAL FINAL: $%.2f\n", currentSale.getTotal());
        System.out.println("========================================");

        System.out.print("\n¿Confirmar venta e imprimir ticket? (S/N): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("S")) {
            System.out.println("\nProcesando guardado en la base de datos...");
            // Le pasamos el objeto Sale completo al repositorio
            saleProcessor.processSale(currentSale);
        } else {
            System.out.println("Venta cancelada.");
        }
    }

    private void registerClient() {
        System.out.println("\n=== INGRESAR NUEVO CLIENTE ===");
        System.out.print("Nombre del cliente: ");
        String name = scanner.nextLine();

        System.out.print("DNI del cliente: ");
        String dni = scanner.nextLine();

        Client newClient = new Client(name, dni);
        clientManager.saveClient(newClient);
    }

    private void consultProduct() {
        System.out.println("\n=== CONSULTAR PRODUCTO ===");
        System.out.print("Ingrese el ID del producto: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Product product = productConsultant.getProductById(id);

            if (product != null) {
                System.out.println("Producto encontrado:");
                System.out.println("- Nombre: " + product.getName());
                System.out.println("- Precio: $" + product.getPrice());
                System.out.println("- Stock disponible: " + product.getStock() + " unidades");

                // Podemos usar el polimorfismo acá también para darle info extra al usuario
                if (product.miFarmacityDiscount()) {
                    System.out.println("- Promoción: Acepta descuento de Mi Farmacity");
                } else {
                    System.out.println("- Promoción: NO acepta descuentos (Venta bajo receta)");
                }
            } else {
                System.out.println("No se encontro ningun producto con el ID " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("El ID debe ser un número válido.");
        }
    }
}