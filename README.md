# Cafetería - Sistema de Descuentos

Aplicación de escritorio en **Java** con interfaz gráfica **Swing** para registrar pedidos de una cafetería. Calcula automáticamente el descuento de cada pedido mediante una **expresión lambda** y guarda cada venta en dos lugares: una base de datos **SQLite** (`cafeteria.db`) y un archivo **CSV** (`datos.csv`).

## Características principales

* **Formulario de pedidos:** registra el nombre del cliente, el nombre del producto, el tipo (Bebida o Comida) y el monto del pedido.
* **Validación de datos:** el cliente y el producto son obligatorios, y el monto debe ser un número positivo. Se acepta coma o punto como separador decimal.
* **Descuento con lambda:** la regla se define como una interfaz funcional (`ReglaDescuento`) y se implementa con una lambda:

  | Monto del pedido | Descuento |
  |------------------|-----------|
  | Mayor a $10.000  | 15 % del monto |
  | $10.000 o menos  | Sin descuento |

* **POO:** la clase abstracta `Producto` tiene dos subclases, `Bebida` y `Comida`, que sobrescriben `getTipo()` (herencia y polimorfismo).
* **Doble persistencia:**
  * Inserta el pedido en la tabla `pedidos` de SQLite, con fecha y hora local.
  * Agrega una línea a `datos.csv`, que se crea con encabezado si no existe.
* **Estado de la base de datos al iniciar:** al abrir la ventana se crea la tabla si no existe y se muestra el resultado de la conexión.

## Tecnologías y conceptos

* **Lenguaje:** Java. Requiere **JDK 15 o superior** porque usa *text blocks* (`"""`). Se recomienda **JDK 21 (LTS)**, que es la versión con la que están compilados los `.class` de `bin/`.
* **GUI:** Java Swing (`JFrame`, `JPanel`, `JComboBox`, `JTextArea`, `JOptionPane`).
* **Base de datos:** SQLite mediante el controlador JDBC `sqlite-jdbc-3.53.4.0.jar` (incluido en `src/lib/`).
* **Archivos:** `java.nio.file` para escribir el CSV en UTF-8.
* **Principios aplicados:**
  * **POO:** abstracción, herencia y polimorfismo.
  * **SOLID:**
    * *Responsabilidad única:* la interfaz, la lógica de descuento y la persistencia están en clases separadas.
    * *Abierto/cerrado:* `CalculadoraDescuento` recibe cualquier `ReglaDescuento`, así que se pueden agregar reglas nuevas sin modificarla.

## Estructura del proyecto

```text
.
├── README.md
├── datos.csv                    # Registro de ventas generado por la aplicación
├── bin/                         # Clases compiladas (Java 21)
└── src/
    ├── Main.java                # Punto de entrada: abre la ventana en el hilo de Swing
    ├── modelo/
    │   ├── Producto.java        # Clase abstracta base (nombre, precio, getTipo())
    │   ├── Bebida.java          # Producto con tamaño
    │   └── Comida.java          # Producto con indicador vegetariano
    ├── descuento/
    │   ├── ReglaDescuento.java        # Interfaz funcional
    │   └── CalculadoraDescuento.java  # Aplica la regla y valida el monto
    ├── persistencia/
    │   ├── ConexionBD.java      # Conexión SQLite, creación de tabla e inserción
    │   └── ArchivoCSV.java      # Escritura de ventas en datos.csv
    ├── vista/
    │   └── VentanaCafeteria.java  # Ventana Swing y flujo del pedido
    └── lib/
        └── sqlite-jdbc-3.53.4.0.jar  # Controlador JDBC de SQLite
```

## Requisitos

* **JDK 15 o superior** (se recomienda 21). Comprueba la versión con:

  ```bash
  java -version
  javac -version
  ```

  > En Windows puede pasar que `javac` sea moderno pero `java` apunte a un Java 8 antiguo (por ejemplo, `C:\Program Files (x86)\Common Files\Oracle\Java\java8path`). En ese caso aparecerá el error `UnsupportedClassVersionError`. Para solucionarlo, ejecuta el `java` del mismo JDK que usaste para compilar o ajusta el `PATH`.

## Cómo compilar y ejecutar

Todos los comandos se ejecutan desde la **raíz del repositorio**.

### Windows (PowerShell)

```powershell
# 1. Compilar
javac -cp "src/lib/*" -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName

# 2. Ejecutar
java -cp "bin;src/lib/*" Main
```

### Linux / macOS (bash)

```bash
# 1. Compilar
javac -cp "src/lib/*" -d bin $(find src -name "*.java")

# 2. Ejecutar
java -cp "bin:src/lib/*" Main
```

> Observa que el separador del classpath es `;` en Windows y `:` en Linux/macOS.
>
> Con JDK 24 o superior, el controlador SQLite muestra una advertencia de *restricted method*. Puedes silenciarla añadiendo `--enable-native-access=ALL-UNNAMED` al comando `java`.

## Uso

1. Abre la aplicación. El área de resultados muestra el estado de la conexión con SQLite.
2. Escribe el nombre del cliente y el del producto, elige el tipo y escribe el monto.
3. Pulsa **Calcular Descuento**. Se muestran el monto, el descuento y el total a pagar, junto con el resultado del guardado en SQLite y en CSV.

## Datos generados

Los dos archivos se crean en el **directorio desde el que se ejecuta** la aplicación.

### `cafeteria.db` (SQLite), tabla `pedidos`

| Columna       | Tipo    | Descripción                       |
|---------------|---------|-----------------------------------|
| `id`          | INTEGER | Clave primaria autoincremental    |
| `cliente`     | TEXT    | Nombre del cliente                |
| `producto`    | TEXT    | Nombre del producto               |
| `tipo`        | TEXT    | `Bebida` o `Comida`               |
| `monto`       | REAL    | Monto del pedido                  |
| `descuento`   | REAL    | Descuento aplicado                |
| `total_pagar` | REAL    | Monto menos el descuento          |
| `fecha`       | TEXT    | Fecha y hora local del registro   |

### `datos.csv`

Usa `;` como separador y `.` como separador decimal:

```text
Cliente;Producto;Tipo;Monto;Descuento;Total
Maria;Palito de queso;Comida;25000.00;3750.00;21250.00
```

Si un campo de texto contiene `;` o saltos de línea, estos se reemplazan para no romper el formato.

## Limitaciones conocidas

* El tamaño de la bebida (`"Mediano"`) y si la comida es vegetariana (`false`) están fijos en el código; el formulario no los pide.
* El precio del producto es el mismo monto que se escribe en el formulario; no existe un catálogo de productos.
* La regla de descuento (15 % sobre $10.000) está definida dentro de `VentanaCafeteria`. Cambiarla requiere editar el código.
* En `src/lib/` hay archivos de marcador vacíos (`documento`, `evidencias`, `datos.csv`) y una copia antigua de este README.
