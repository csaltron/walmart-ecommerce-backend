# Catálogo Service - Backend API

API REST para gestionar catálogo de productos de un e-commerce desarrollada con Java 17 y Spring Boot 3.

## Arquitectura

El proyecto sigue principios de **Clean Architecture** y **SOLID**, con separación clara de capas:

```
├── domain/               # Capa de dominio (entidades y contratos)
│   ├── entities/         # Entidades de negocio
│   └── repositories/     # Interfaces de repositorio
│   └── exceptions/       # Excepciones de dominio
├── application/          # Capa de aplicación (casos de uso)
│   ├── dto/              # DTOs y mappers
│   └── service/          # Servicios de aplicación
└── infrastructure/       # Capa de infraestructura
    ├── persistence/      # Implementación de repositorios
    ├── web/              # Controllers REST
    └── config/           # Configuraciones
```

## Stack Tecnológico

- Java 17
- Spring Boot 3.2.1
- Spring Data MongoDB
- Lombok
- SpringDoc OpenAPI (Swagger)
- Maven

## Características

### Funcionalidades Core
- Listado paginado de productos
- Búsqueda por texto en nombre y descripción
- Filtros múltiples: categoría, marca, rango de precio, stock, tags
- Ordenamiento configurable (precio, nombre, stock)
- Consulta de detalle de producto
- Listado de categorías y marcas disponibles

### Aspectos Técnicos
- Índices de texto en MongoDB para búsqueda eficiente
- Paginación optimizada con cursor-based approach
- Validación de entrada con Bean Validation
- Manejo centralizado de excepciones
- Documentación OpenAPI/Swagger
- CORS habilitado para desarrollo
- Logging estructurado

## Requisitos Previos

- Java 17 o superior
- Maven 3.8+
- Docker y Docker Compose (para MongoDB)

## Instalación y Ejecución

### 1. Clonar el repositorio
```html'
https://github.com/csaltron/walmart-ecommerce-backend.git
```

```bash
cd catalog-backend
```

### 2. Iniciar MongoDB

```bash
docker-compose up -d
```

Esto levantará MongoDB en `localhost:27017`.

### 3. Compilar el proyecto

```bash
./mvnw clean package
```

### 4. Ejecutar la aplicación

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

La API estará disponible en: `http://localhost:8080/api`

### 5. Verificar la instalación

Acceder a la documentación Swagger:
```
http://localhost:8080/api/swagger-ui/index.html
```

## Endpoints Principales

### Búsqueda y Filtrado
```
GET /api/v1/products
```

Parámetros opcionales:
- `search`: Texto a buscar en nombre/descripción
- `category`: Filtrar por categoría
- `brand`: Filtrar por marca
- `minPrice`: Precio mínimo
- `maxPrice`: Precio máximo
- `inStock`: Solo productos disponibles (true/false)
- `tags`: Lista de tags
- `page`: Número de página (default: 0)
- `size`: Tamaño de página (default: 20)
- `sortBy`: Campo de ordenamiento (price, name, stock)
- `sortDirection`: Dirección (asc, desc)

Ejemplos:
```bash
# Buscar productos con "zapatillas"
curl "http://localhost:8080/api/v1/products?search=zapatillas"

# Filtrar por categoría y rango de precio
curl "http://localhost:8080/api/v1/products?category=Ropa&minPrice=20&maxPrice=50"

# Ordenar por precio descendente
curl "http://localhost:8080/api/v1/products?sortBy=price&sortDirection=desc"

# Múltiples filtros combinados
curl "http://localhost:8080/api/v1/products?category=Electrónica&brand=SoundMax&inStock=true&page=0&size=10"
```

### Detalle de Producto
```
GET /api/v1/products/{id}
```

Ejemplo:
```bash
curl "http://localhost:8080/api/v1/products/p-001"
```

### Obtener Categorías
```
GET /api/v1/products/categories
```

### Obtener Marcas
```
GET /api/v1/products/brands
```

## Formato de Respuesta

### Producto Individual
```json
{
  "id": "p-001",
  "name": "Zapatillas Runner X",
  "description": "Zapatillas para running, suela EVA, talle 40-45",
  "category": "Calzado",
  "brand": "SportCo",
  "price": 79.99,
  "oldPrice": 99.99,
  "stock": 25,
  "tags": ["running", "outdoor"],
  "imageUrl": "https://example.com/img/p-001.jpg",
  "available": true,
  "discountPercentage": 20
}
```

### Lista Paginada
```json
{
  "content": [...],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5,
  "first": true,
  "last": false
}
```

## Escalabilidad

### Estrategias para Mayor Volumen

#### 1. Base de Datos
- **Índices optimizados**: Índices compuestos para filtros frecuentes
- **Sharding**: Particionar por categoría o región geográfica
- **Réplicas de lectura**: Para distribuir consultas de búsqueda
- **Cache distribuido**: Redis para productos populares y resultados de búsqueda más rápidas

#### 2. Aplicación
- **Instancias múltiples**: Escalado horizontal con load balancer
- **Cache local**: Caffeine para metadatos (categorías, marcas)
- **Async processing**: Cola de mensajes para operaciones no críticas
- **Rate limiting**: Throttling de peticiones por cliente

#### 3. Búsqueda
- **Elasticsearch**: Para búsqueda avanzada y faceted search
- **Indexación incremental**: Sincronización con MongoDB vía Change Streams
- **Search suggestions**: Autocompletado con tries o prefix search

#### 4. Arquitectura
```
┌─────────────┐
│ Load        │
│ Balancer    │
└──────┬──────┘
       │
   ┌───┴────┐
   │        │
┌──▼──┐  ┌─▼───┐
│ API │  │ API │ (N instancias)
└──┬──┘  └─┬───┘
   │       │
   └───┬───┘
       │
┌──────▼──────┐
│   Redis     │ (Cache)
└──────┬──────┘
       │
┌──────▼──────────┐
│  Elasticsearch  │ (Búsqueda)
└──────┬──────────┘
       │
┌──────▼──────┐
│  MongoDB    │ (Datos maestros)
│  + Réplicas │
└─────────────┘
```
## Testing

Ejecutar tests:
```bash
./mvnw test
```

## Consideraciones de Producción

### Seguridad
- Agregar Spring Security con JWT
- Rate limiting por IP/usuario
- Validación exhaustiva de entrada
- HTTPS obligatorio

### Monitoreo
- Spring Boot Actuator para métricas
- Prometheus + Grafana para visualización
- Logs centralizados (ELK Stack)
- Distributed tracing (Jaeger)

### CI/CD
- Pipeline con GitHub Actions / Jenkins
- Tests automáticos en cada commit
- Deploy automatizado a staging/producción
- Health checks y rollback automático

## Configuración Adicional

### Variables de Entorno
```bash
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/catalog_db
SERVER_PORT=8080
APP_PAGINATION_DEFAULT_PAGE_SIZE=20
```

### Perfiles de Spring
- `dev`: Desarrollo local
- `test`: Testing
- `prod`: Producción

Activar perfil:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## Troubleshooting

### MongoDB no conecta
Verificar que Docker esté corriendo:
```bash
docker ps
```

Revisar logs:
```bash
docker logs catalog-mongodb
```

### Puerto 8080 ocupado
Cambiar puerto en `application.yml` o variable de entorno:
```bash
SERVER_PORT=8081 ./mvnw spring-boot:run
```

## Contacto y Soporte
- Documentación API: http://localhost:8080/api/swagger-ui.html
