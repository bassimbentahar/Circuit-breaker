# Circuit-breaker


Le but est d’implémenter le pattern « Circuit breaker » avec 3 états
(open, closed, half_open)
Etapes :
• Implémenter le pattern à partir de la présentation en classe en suivant les étapes
suivantes:
1) D’abord le circuit breaker est fermé
2) Si la requête déclenche un timeout on ouvre le “circuit breaker”.
3) Les 4 requêtes suivantes déclenchent une exception « CircuitBreakerException”, ce qui a pour conséquence de ne pas envoyer les
   requêtes au serveur et permettre au client de trouver une alternative.

4) A la 5ème requête on ouvre le “circuit breaker” à moitié, dans cet état on exécute 3 fois la requête pour vérifier l’état du serveur :
  ▪ Si les 3 requêtes déclenchent un timeout on re-ouvre le “circuit
  breaker” et retour à l’état 2.
  ▪ Si non on déduit que le serveur est revenu à son état normal et on
  ferme le “circuit breaker”.
  
