import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
from matplotlib.patches import FancyBboxPatch, FancyArrowPatch
import numpy as np

# Configuración de la figura
fig, ax = plt.subplots(figsize=(16, 10))
ax.set_xlim(0, 10)
ax.set_ylim(0, 10)
ax.axis('off')
fig.patch.set_facecolor('#f8f9fa')

# Colores personalizados
color_central = '#2C3E50'
color_liderazgo = '#3498DB'
color_clima = '#E74C3C'
color_relacion = '#27AE60'
color_subnodos = '#95A5A6'

# Función para crear cajas redondeadas con sombra
def crear_nodo(ax, x, y, texto, color, ancho=1.5, alto=0.6, fontsize=11, peso='bold'):
    # Sombra
    sombra = FancyBboxPatch(
        (x - ancho/2 + 0.03, y - alto/2 - 0.03),
        ancho, alto,
        boxstyle="round,pad=0.1",
        facecolor='gray',
        alpha=0.3,
        zorder=1
    )
    ax.add_patch(sombra)
    
    # Nodo principal
    nodo = FancyBboxPatch(
        (x - ancho/2, y - alto/2),
        ancho, alto,
        boxstyle="round,pad=0.1",
        facecolor=color,
        edgecolor='white',
        linewidth=2,
        zorder=2
    )
    ax.add_patch(nodo)
    
    # Texto
    ax.text(x, y, texto, ha='center', va='center',
            fontsize=fontsize, color='white', weight=peso, zorder=3)

# Función para crear conexiones curvas
def crear_conexion(ax, x1, y1, x2, y2, color, curvatura=0.3):
    arrow = FancyArrowPatch(
        (x1, y1), (x2, y2),
        arrowstyle='-',
        connectionstyle=f"arc3,rad={curvatura}",
        color=color,
        linewidth=3,
        alpha=0.6,
        zorder=0
    )
    ax.add_patch(arrow)

# NODO CENTRAL
crear_nodo(ax, 5, 5, 'LIDERAZGO Y\nCLIMA LABORAL', color_central, 2.2, 1, 14, 'bold')

# ============ RAMA LIDERAZGO (Izquierda) ============
crear_conexion(ax, 3.9, 5, 2.5, 7, color_liderazgo, 0.5)
crear_nodo(ax, 1.5, 7.5, 'LIDERAZGO', color_liderazgo, 1.6, 0.7, 12)

# Subnodos de Liderazgo
subnodos_liderazgo = [
    (0.8, 6.5, 'Proceso de\ninfluencia'),
    (2.2, 6.5, 'Motivación y guía'),
    (0.8, 5.5, 'Toma de\ndecisiones'),
]

for x, y, texto in subnodos_liderazgo:
    crear_conexion(ax, 1.5, 7, x, y, color_subnodos, 0.2)
    crear_nodo(ax, x, y, texto, color_subnodos, 1.3, 0.5, 9, 'normal')

# ============ ESTILOS DE LIDERAZGO (Izquierda abajo) ============
crear_conexion(ax, 3.9, 4.5, 2.5, 3, color_liderazgo, -0.5)
crear_nodo(ax, 1.5, 2.5, 'ESTILOS', color_liderazgo, 1.4, 0.6, 11)

estilos = [
    (0.5, 1.5, 'Autocrático'),
    (1.5, 1.5, 'Democrático'),
    (2.5, 1.5, 'Laissez-faire'),
    (0.5, 0.7, 'Transformacional'),
    (1.8, 0.7, 'Transaccional'),
]

for x, y, texto in estilos:
    crear_conexion(ax, 1.5, 2.2, x, y, color_subnodos, 0.15)
    crear_nodo(ax, x, y, texto, color_subnodos, 1.2, 0.4, 8, 'normal')

# ============ RAMA CLIMA LABORAL (Derecha) ============
crear_conexion(ax, 6.1, 5, 7.5, 7, color_clima, -0.5)
crear_nodo(ax, 8.5, 7.5, 'CLIMA LABORAL', color_clima, 1.7, 0.7, 12)

# Subnodos de Clima Laboral
subnodos_clima = [
    (7.8, 6.5, 'Percepción del\nentorno'),
    (9.2, 6.5, 'Motivación'),
    (7.8, 5.5, 'Productividad'),
]

for x, y, texto in subnodos_clima:
    crear_conexion(ax, 8.5, 7, x, y, color_subnodos, -0.2)
    crear_nodo(ax, x, y, texto, color_subnodos, 1.3, 0.5, 9, 'normal')

# ============ FACTORES DEL CLIMA (Derecha abajo) ============
crear_conexion(ax, 6.1, 4.5, 7.5, 3, color_clima, 0.5)
crear_nodo(ax, 8.5, 2.5, 'FACTORES', color_clima, 1.4, 0.6, 11)

factores = [
    (7.3, 1.5, 'Comunicación'),
    (8.5, 1.5, 'Relaciones'),
    (9.7, 1.5, 'Condiciones'),
    (7.8, 0.7, 'Reconocimiento'),
    (9.2, 0.7, 'Desarrollo'),
]

for x, y, texto in factores:
    crear_conexion(ax, 8.5, 2.2, x, y, color_subnodos, -0.15)
    crear_nodo(ax, x, y, texto, color_subnodos, 1.1, 0.4, 8, 'normal')

# ============ RELACIÓN (Abajo centro) ============
crear_conexion(ax, 5, 4.5, 5, 3.5, color_relacion, 0)
crear_nodo(ax, 5, 3.2, 'RELACIÓN', color_relacion, 1.4, 0.6, 11)

relaciones = [
    (3.8, 2.2, 'Liderazgo +\nClima sano'),
    (6.2, 2.2, 'Mal liderazgo\nConflictos'),
]

for x, y, texto in relaciones:
    crear_conexion(ax, 5, 2.9, x, y, color_subnodos, 0.2)
    crear_nodo(ax, x, y, texto, color_subnodos, 1.3, 0.5, 8, 'normal')

# Título
plt.title('Mapa Mental: Liderazgo y Clima Laboral', 
          fontsize=18, weight='bold', pad=20, color=color_central)

plt.tight_layout()
plt.savefig('mapa_mental_liderazgo.png', dpi=300, bbox_inches='tight', 
            facecolor='#f8f9fa')
plt.show()

print("✓ Mapa mental generado exitosamente como 'mapa_mental_liderazgo.png'")