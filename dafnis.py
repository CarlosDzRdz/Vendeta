from docx import Document
from docx.shared import Pt, Inches, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from datetime import datetime

def crear_reporte_ml():
    """
    Genera un documento Word con un reporte completo sobre algoritmos
    de regresión y clasificación en aprendizaje supervisado.
    """
    doc = Document()
    
    # Configurar márgenes
    sections = doc.sections
    for section in sections:
        section.top_margin = Inches(1)
        section.bottom_margin = Inches(1)
        section.left_margin = Inches(1)
        section.right_margin = Inches(1)
    
    # ==================== PORTADA ====================
    # Nombre de la institución
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = p.add_run("INSTITUTO TECNOLÓGICO SUPERIOR\n")
    run.font.size = Pt(14)
    run.font.bold = True
    
    doc.add_paragraph()
    
    # Materia
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = p.add_run("Materia: Inteligencia Artificial y Aprendizaje Automático\n\n")
    run.font.size = Pt(12)
    
    # Título
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = p.add_run("REPORTE DE INVESTIGACIÓN\n")
    run.font.size = Pt(16)
    run.font.bold = True
    
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = p.add_run("Algoritmos de Regresión y Clasificación\nen Aprendizaje Supervisado\n\n")
    run.font.size = Pt(14)
    run.font.bold = True
    
    doc.add_paragraph()
    doc.add_paragraph()
    
    # Nombre del alumno (editable)
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = p.add_run("Elaborado por:\n")
    run.font.size = Pt(12)
    run = p.add_run("[Nombre del Alumno]\n\n")
    run.font.size = Pt(12)
    run.font.bold = True
    
    # Fecha
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    fecha_actual = datetime.now().strftime("%d de %B de %Y")
    run = p.add_run(f"Fecha: {fecha_actual}")
    run.font.size = Pt(12)
    
    # Salto de página
    doc.add_page_break()
    
    # ==================== INTRODUCCIÓN ====================
    titulo = doc.add_heading("Introducción", level=1)
    titulo.alignment = WD_ALIGN_PARAGRAPH.LEFT
    
    intro_text = """El aprendizaje supervisado constituye uno de los paradigmas fundamentales del machine learning, donde los algoritmos aprenden a partir de datos etiquetados para realizar predicciones o clasificaciones sobre información nueva. Este enfoque se ha convertido en una herramienta esencial en la era del big data, permitiendo a las organizaciones extraer conocimiento valioso de grandes volúmenes de información estructurada.

La relevancia del aprendizaje supervisado radica en su capacidad para abordar dos categorías principales de problemas: la regresión, que predice valores continuos, y la clasificación, que asigna etiquetas o categorías discretas. Estos algoritmos han revolucionado industrias enteras, desde la medicina predictiva hasta el análisis financiero, pasando por sistemas de recomendación y detección de fraudes.

El presente reporte tiene como propósito investigar, analizar y comprender los principales algoritmos de regresión y clasificación utilizados en el aprendizaje supervisado, identificando sus características, funcionamiento, métricas de evaluación y aplicaciones prácticas en contextos reales. A través de este análisis, se busca desarrollar un conocimiento sólido que permita seleccionar el algoritmo más adecuado según las características específicas de cada problema."""
    
    p = doc.add_paragraph(intro_text)
    p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    
    # ==================== DESARROLLO ====================
    doc.add_heading("Desarrollo", level=1)
    
    # Aprendizaje Supervisado
    doc.add_heading("1. Descripción General del Aprendizaje Supervisado", level=2)
    
    supervised_text = """El aprendizaje supervisado es una técnica de machine learning en la cual un modelo se entrena utilizando un conjunto de datos etiquetados, es decir, datos donde cada ejemplo de entrada está asociado con su correspondiente salida correcta. El objetivo principal es que el algoritmo aprenda la relación funcional entre las variables de entrada (features) y la variable de salida (target), para posteriormente realizar predicciones precisas sobre datos no vistos.

Este proceso se fundamenta en la minimización de una función de pérdida que mide la diferencia entre las predicciones del modelo y los valores reales. A través de iteraciones sucesivas, el algoritmo ajusta sus parámetros internos para reducir este error, mejorando progresivamente su capacidad predictiva. La efectividad del modelo depende de la calidad y representatividad de los datos de entrenamiento, así como de la selección apropiada del algoritmo según la naturaleza del problema."""
    
    p = doc.add_paragraph(supervised_text)
    p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    
    # Algoritmos de Regresión
    doc.add_heading("2. Algoritmos de Regresión", level=2)
    
    doc.add_heading("2.1 Regresión Lineal", level=3)
    regresion_lineal = """La regresión lineal es uno de los algoritmos más fundamentales y utilizados en el análisis predictivo. Establece una relación lineal entre una o más variables independientes y una variable dependiente continua. Su formulación matemática busca encontrar la línea (o hiperplano en múltiples dimensiones) que mejor se ajusta a los datos minimizando la suma de los errores cuadráticos. Es especialmente útil cuando existe una relación proporcional entre variables y se requiere interpretabilidad del modelo."""
    
    p = doc.add_paragraph(regresion_lineal)
    p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    
    doc.add_heading("2.2 Regresión Polinómica", level=3)
    regresion_poli = """La regresión polinómica extiende el concepto de regresión lineal permitiendo capturar relaciones no lineales entre variables mediante la incorporación de términos polinómicos. Al transformar las características originales en potencias de diferente grado, el modelo puede ajustarse a patrones más complejos en los datos. Sin embargo, requiere cuidado para evitar el sobreajuste (overfitting), especialmente con polinomios de grado elevado."""
    
    p = doc.add_paragraph(regresion_poli)
    p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    
    doc.add_heading("2.3 Árboles de Decisión para Regresión", level=3)
    arboles_reg = """Los árboles de decisión para regresión dividen recursivamente el espacio de características en regiones, asignando a cada región un valor de predicción basado en el promedio de las muestras que contiene. Este enfoque no paramétrico puede capturar relaciones complejas y no lineales sin requerir transformaciones previas de los datos. Son especialmente valiosos por su interpretabilidad y capacidad para manejar variables categóricas y numéricas simultáneamente."""
    
    p = doc.add_paragraph(arboles_reg)
    p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    
    # Algoritmos de Clasificación
    doc.add_heading("3. Algoritmos de Clasificación", level=2)
    
    doc.add_heading("3.1 K-Nearest Neighbors (KNN)", level=3)
    knn_text = """K-Nearest Neighbors es un algoritmo basado en instancias que clasifica un nuevo punto de datos según la clase mayoritaria entre sus k vecinos más cercanos en el espacio de características. Su simplicidad conceptual y ausencia de fase de entrenamiento explícita lo hacen atractivo para problemas de clasificación multiclase. La selección del valor k y la métrica de distancia son decisiones críticas que afectan significativamente su rendimiento."""
    
    p = doc.add_paragraph(knn_text)
    p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    
    doc.add_heading("3.2 Naïve Bayes", level=3)
    naive_text = """El clasificador Naïve Bayes se fundamenta en el teorema de Bayes y asume independencia condicional entre las características. A pesar de esta suposición simplificadora, que rara vez se cumple en la práctica, el algoritmo demuestra un rendimiento sorprendentemente efectivo en muchos escenarios reales. Es particularmente eficiente para clasificación de texto y análisis de sentimientos, ofreciendo rapidez en el entrenamiento y la predicción."""
    
    p = doc.add_paragraph(naive_text)
    p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    
    doc.add_heading("3.3 Support Vector Machine (SVM)", level=3)
    svm_text = """Las Máquinas de Vectores de Soporte buscan encontrar el hiperplano óptimo que maximiza el margen entre clases en el espacio de características. Mediante el uso del kernel trick, SVM puede transformar problemas no linealmente separables en espacios de mayor dimensión donde sí lo son. Este algoritmo es robusto ante datos de alta dimensionalidad y efectivo en escenarios donde el número de dimensiones excede el número de muestras."""
    
    p = doc.add_paragraph(svm_text)
    p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    
    doc.add_heading("3.4 Redes Neuronales", level=3)
    nn_text = """Las redes neuronales artificiales son modelos computacionales inspirados en el sistema nervioso biológico, compuestos por capas de neuronas interconectadas. Su capacidad para aprender representaciones jerárquicas de los datos las hace extraordinariamente poderosas para problemas complejos. El deep learning, con arquitecturas profundas, ha revolucionado campos como visión por computadora, procesamiento de lenguaje natural y reconocimiento de patrones, aunque requiere grandes volúmenes de datos y recursos computacionales significativos."""
    
    p = doc.add_paragraph(nn_text)
    p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    
    # Proceso de Entrenamiento
    doc.add_heading("4. Proceso General de Entrenamiento y Validación", level=2)
    
    proceso_text = """El proceso de entrenamiento y validación de modelos supervisados sigue una metodología sistemática que garantiza su generalización a datos nuevos. Primero, se divide el conjunto de datos en tres subconjuntos: entrenamiento (típicamente 60-70%), validación (15-20%) y prueba (15-20%). El conjunto de entrenamiento se utiliza para ajustar los parámetros del modelo, mientras que el conjunto de validación ayuda a optimizar hiperparámetros y prevenir el sobreajuste.

La validación cruzada (cross-validation) es una técnica robusta que particiona los datos en k pliegues, entrenando el modelo k veces y utilizando cada pliegue como conjunto de validación una vez. Esto proporciona una estimación más confiable del rendimiento del modelo. Finalmente, el conjunto de prueba, que permanece completamente independiente durante todo el desarrollo, se utiliza para evaluar el rendimiento final del modelo y reportar métricas objetivas de su capacidad predictiva."""
    
    p = doc.add_paragraph(proceso_text)
    p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    
    # Ejemplos de Uso
    doc.add_heading("5. Ejemplos de Uso en Contextos Reales", level=2)
    
    ejemplos_text = """Los algoritmos de aprendizaje supervisado tienen aplicaciones transformadoras en múltiples sectores:

Sector Médico: La regresión se emplea para predecir la progresión de enfermedades crónicas o dosificación de medicamentos, mientras que la clasificación permite diagnósticos automatizados mediante el análisis de imágenes médicas (rayos X, resonancias magnéticas) para detectar tumores, neumonía o retinopatía diabética. Los algoritmos de redes neuronales profundas han alcanzado precisión comparable o superior a especialistas humanos en ciertas tareas diagnósticas.

Sector Financiero: Los modelos de clasificación son fundamentales en la evaluación de riesgo crediticio, determinando la probabilidad de incumplimiento de préstamos. La regresión se utiliza para predicción de precios de acciones y análisis de series temporales financieras. Los sistemas de detección de fraude emplean algoritmos como Random Forest y redes neuronales para identificar transacciones sospechosas en tiempo real, protegiendo a millones de usuarios.

Sector Industrial: En manufactura predictiva, los algoritmos de regresión estiman el tiempo de vida útil de maquinaria y componentes, permitiendo mantenimiento preventivo que reduce costos operativos. Los sistemas de control de calidad utilizan clasificación mediante visión por computadora para identificar defectos en líneas de producción con velocidades y precisión superiores a la inspección humana.

Sector Comercial: Los sistemas de recomendación en plataformas de e-commerce y streaming utilizan regresión para predecir calificaciones de usuarios y clasificación para categorizar preferencias. El análisis de sentimientos en redes sociales emplea Naïve Bayes y redes neuronales para comprender la percepción de marca y productos en tiempo real."""
    
    p = doc.add_paragraph(ejemplos_text)
    p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    
    # ==================== MÉTRICAS Y EVALUACIÓN ====================
    doc.add_heading("Métricas y Evaluación", level=1)
    
    doc.add_heading("Métricas para Algoritmos de Regresión", level=2)
    
    metricas_reg = """Error Cuadrático Medio (MSE - Mean Squared Error): Calcula el promedio de los errores al cuadrado entre los valores predichos y reales. Penaliza fuertemente los errores grandes debido a la elevación al cuadrado, haciendo que el modelo sea sensible a outliers. Es útil cuando se desea minimizar errores significativos.

Coeficiente de Determinación (R²): Representa la proporción de la varianza en la variable dependiente que es predecible a partir de las variables independientes. Los valores oscilan entre 0 y 1, donde 1 indica un ajuste perfecto. Es intuitivo para interpretar qué tan bien el modelo explica la variabilidad de los datos.

Error Absoluto Medio (MAE - Mean Absolute Error): Promedia el valor absoluto de los errores, proporcionando una medida en las mismas unidades que la variable objetivo. A diferencia del MSE, no penaliza desproporcionadamente los errores grandes, siendo más robusta ante outliers y más fácil de interpretar."""
    
    p = doc.add_paragraph(metricas_reg)
    p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    
    doc.add_heading("Métricas para Algoritmos de Clasificación", level=2)
    
    metricas_clas = """Exactitud (Accuracy): Proporción de predicciones correctas sobre el total de predicciones. Es la métrica más intuitiva pero puede ser engañosa en conjuntos de datos desbalanceados, donde una clase predomina sobre otras.

Precisión (Precision): De todas las instancias que el modelo predijo como positivas, cuántas realmente lo son. Es crucial en aplicaciones donde los falsos positivos son costosos, como diagnósticos médicos o detección de spam.

Sensibilidad o Recall: De todas las instancias que realmente son positivas, cuántas fueron correctamente identificadas por el modelo. Es fundamental en aplicaciones donde los falsos negativos son críticos, como detección de enfermedades o fraudes.

F1 Score: Media armónica entre precisión y recall, proporcionando un balance entre ambas métricas. Es especialmente útil cuando se necesita un equilibrio entre minimizar falsos positivos y falsos negativos, o cuando los datos están desbalanceados."""
    
    p = doc.add_paragraph(metricas_clas)
    p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    
    # ==================== CONCLUSIONES ====================
    doc.add_heading("Conclusiones", level=1)
    
    conclusiones = """La selección del algoritmo más adecuado para un problema de aprendizaje supervisado depende de múltiples factores que deben evaluarse cuidadosamente. La naturaleza de los datos es el primer criterio fundamental: para relaciones lineales simples, la regresión lineal ofrece interpretabilidad y eficiencia; sin embargo, cuando los datos presentan patrones complejos y no lineales, algoritmos como redes neuronales o árboles de decisión ensemble pueden proporcionar mayor precisión predictiva.

El tamaño del conjunto de datos es otro factor determinante. Los algoritmos paramétricos simples como la regresión lineal o Naïve Bayes funcionan bien con conjuntos de datos pequeños, mientras que las redes neuronales profundas requieren grandes volúmenes de datos para evitar el sobreajuste y explotar completamente su capacidad representacional. La dimensionalidad también influye: SVM maneja eficientemente espacios de alta dimensión, mientras que KNN puede sufrir la "maldición de la dimensionalidad".

La interpretabilidad versus rendimiento representa un trade-off crucial. En aplicaciones críticas como diagnóstico médico o decisiones legales, la capacidad de explicar las predicciones (característica de modelos lineales o árboles de decisión) puede ser más valiosa que una mejora marginal en precisión obtenida con modelos tipo "caja negra" como redes neuronales profundas.

Los requisitos computacionales y de tiempo también son consideraciones prácticas importantes. Algoritmos como Naïve Bayes y regresión lineal entrenan rápidamente, siendo ideales para aplicaciones en tiempo real, mientras que las redes neuronales pueden requerir horas o días de entrenamiento en hardware especializado.

En conclusión, no existe un algoritmo universalmente superior; la elección óptima emerge del análisis cuidadoso de las características específicas del problema, los datos disponibles, los recursos computacionales y los requisitos del dominio de aplicación. La experimentación sistemática con múltiples algoritmos y la validación rigurosa mediante métricas apropiadas constituyen la metodología más confiable para identificar la solución más efectiva para cada caso particular."""
    
    p = doc.add_paragraph(conclusiones)
    p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    
    # ==================== REFERENCIAS ====================
    doc.add_page_break()
    doc.add_heading("Referencias", level=1)
    
    referencias = [
        "Géron, A. (2022). Hands-on machine learning with Scikit-Learn, Keras, and TensorFlow (3rd ed.). O'Reilly Media.",
        "Hastie, T., Tibshirani, R., & Friedman, J. (2021). The elements of statistical learning: Data mining, inference, and prediction (2nd ed.). Springer.",
        "James, G., Witten, D., Hastie, T., & Tibshirani, R. (2021). An introduction to statistical learning: With applications in R (2nd ed.). Springer.",
        "Murphy, K. P. (2022). Probabilistic machine learning: An introduction. MIT Press.",
        "Russell, S., & Norvig, P. (2020). Artificial intelligence: A modern approach (4th ed.). Pearson."
    ]
    
    for ref in referencias:
        p = doc.add_paragraph(ref, style='List Number')
        p.alignment = WD_ALIGN_PARAGRAPH.LEFT
        p_format = p.paragraph_format
        p_format.left_indent = Inches(0.5)
        p_format.first_line_indent = Inches(-0.5)
    
    # Guardar documento
    nombre_archivo = "Reporte_Algoritmos_ML_Supervisado.docx"
    doc.save(nombre_archivo)
    print(f"✓ Documento generado exitosamente: {nombre_archivo}")
    print(f"✓ El reporte contiene aproximadamente 5 cuartillas")
    print(f"✓ Recuerda editar el nombre del alumno en la portada")

# Ejecutar la función
if __name__ == "__main__":
    crear_reporte_ml()