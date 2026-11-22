# Cafetería - Sistema de Descuentos

Este proyecto es una aplicación de escritorio desarrollada en **Java** con una interfaz gráfica basada en **Swing**. Su propósito principal es gestionar los pedidos de una cafetería, calcular descuentos automatizados utilizando programación funcional (expresiones Lambda) y registrar las ventas tanto en una base de datos **SQLite** como en archivos **CSV**.

## Características Principales

*   **Interfaz Gráfica Intuitiva:** Formulario fácil de usar para registrar el cliente, el producto (Bebida o Comida) y el monto del pedido.
*   **Cálculo Dinámico de Descuentos:** Utiliza una interfaz funcional (`ReglaDescuento`) y expresiones lambda para aplicar un **15% de descuento** en compras superiores a $10,000.
*   **Programación Orientada a Objetos (POO):** Implementación de herencia y polimorfismo mediante una clase abstracta `Producto` y sus clases hijas (`Bebida` y `Comida`).
*   **Doble Persistencia de Datos:**
    *   Guarda el registro de ventas en una base de datos relacional (SQLite).
    *   Exporta automáticamente un respaldo del recibo en formato `.csv`.

## Tecnologías y Conceptos Utilizados

*   **Lenguaje:** Java (JDK 8+)
*   **GUI:** Java Swing (`JFrame`, `JPanel`, `JOptionPane`)
*   **Base de Datos:** SQLite (JDBC)
*   **Principios Aplicados:**
    *   **POO:** Abstracción, Herencia y Polimorfismo.
    *   **SOLID:** Principio de Responsabilidad Única (clases separadas para interfaz, lógica de descuento y base de datos) y Principio de Abierto/Cerrado (fácil inyección de nuevas reglas de descuento).

## Estructura del Proyecto

El proyecto está organizado bajo el patrón de arquitectura de capas:
*   `modelo/`: Contiene las entidades del negocio (`Producto`, `Bebida`, `Comida`).
*   `vista/`: Contiene la interfaz de usuario (`VentanaCafeteria`).
*   `descuento/`: Contiene la lógica de negocio y cálculo (`CalculadoraDescuento`, `ReglaDescuento`).
*   `persistencia/`: Maneja el guardado de datos (`ConexionBD`, `ArchivoCSV`).

## Cómo ejecutar el proyecto

**1. Crear carpeta de compilación:**
```bash
mkdir -p bin
```
**2. Compilar todos los archivos java:**
**
```bash
javac -cp "lib/*" -d bin $(find src -name "*.java")
```
**3. Ejecutar aplicación:**
```bash
java -cp "bin;lib/*" Main
```
