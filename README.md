# SmokEE - Java EE 7 Smoke-Testing Framework

## Master Thesis - Multimedia und Softwareentwicklung - Technikum Wien 2015

Die Popularität von agilen Vorgehensmodellen in der Softwareentwicklung sowie die Verwendung von Continuous Integration und Continuous Delivery Systemen führt zwangsläufig zu einer erhöhten Anzahl an Lieferungen und Installationen von Softwarekomponenten. Ein gewisses Maß an Qualität kann dabei nur erreicht werden, wenn nach jeder erfolgten Installation das grundlegende Funktionieren des Systems verifiziert wird. Um die daraus resultierenden erhöhten Testaufwände zu reduzieren, ist es notwendig, die sogenannten Smoke-Tests, die der Überprüfung der Basisfunktionen dienen, möglichst effizient zu gestalten. Die Effizienz kann dadurch gesteigert werden, dass die Tests zum einen möglichst einfach definierbar sind und zum anderen automatisch ausgeführt werden.

Diese Arbeit setzt sich mit der Konzeption eines Smoke-Testing Frameworks auseinander, das auf dem Java EE 7 Stack basiert. Dadurch ist die Verwendung des Frameworks prinzipiell für jede Java EE 7 Applikation möglich, sofern diese in einem Java EE 7 zertifizierten Anwendungsserver betrieben wird. Das Framework erlaubt das einfache Beschreiben von Smoke-Tests mittels Java-Annotationen. Darüber hinaus stellt es eine plattformunabhängige und skriptbare Schnittstelle für die Ausführung der Tests zur Verfügung.

Für die Realisierung des Frameworks wurde auf die Technologien des Java EE 7 Stacks, wie zum Beispiel CDI Erweiterungen, Interzeptoren und JAX-RS, zurückgegriffen. Die Anwendung dieser Technologien wird in dieser Arbeit im Detail behandelt.
