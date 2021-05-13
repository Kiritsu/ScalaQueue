# Scala custom Queue

## Introduction 

Cette implémentation d'une queue (First In, First Out) en scala a la particularité d'être composée de deux sous-listes. Par défaut, seule la liste `in` est utilisée lors d'ajouts d'éléments dans la queue. On peut donc très rapidement récupérer le dernier élément inséré. Lorsque l'on dépile, on utilise la seconde liste `out` qui devient, à chaque fois qu'elle est vide, l'inverse de `in`.

Par exemple, de base, notre Queue d'entiers est vide :
`Queue(Nil, Nil)`
On insère une première valeur : 
`Queue(1;Nil, Nil)`
On insère une seconde valeur : 
`Queue(2;1;Nil, Nil)`

Lorsque l'on veut dépiler, donc récupérer la valeur `1`, on inverse `in` dans `out` et on récupère la nouvelle tête :
`1` - `Queue(Nil, 2;Nil)`

Comme ça, après avoir empilé de nouveaux éléments, si on veut dépiler, l'opération est de `O(1)`, et si on veut récupérer le dernier élément inséré, il s'agit aussi d'une opération `O(1)`.

Avec une telle Queue : `Queue(4;2;1;5;2;4;Nil, 7;6;12;5;2;3;3;2;Nil)`, on pourra dépiler les 8 prochains éléments avec une opération `O(1)` à chaque fois, tout en pouvant avoir accès aux derniers élément insérés rapidement, ce qui rend notre `Queue` modulable : elle pourrait être utilisée en tant que liste, et adaptée en tant que `Queue` lorsque l'on a besoin de dépiler. L'opération la plus longue étant l'inversion de `in` dans `out` `O(n)` pour pouvoir dépiler rapidement `O(1)` ne se fait donc que si nécessaire, et lorsque `out` est vide.

Pour rappel, toutes les méthodes de l'implémentation de cette Queue[T] doivent être totales et pures :

- Fonction pure : sans effets de bord
- Fonction totale : retourne forcément une valeur et tous les cas possibles sont gérés, on utilise en général `Option[T]`.

## Méthodes d'implémentation utilisées

### Représentation de la case class Queue[T]

```scala
Queue[T](in: List[T] = Nil, out: List[T] = Nil):
  enqueue(x: T): Queue[T]
  dequeue(): (Option[T], Queue[T])
  headOption: Option[T]
  rearOption: Option[T]
  toList: List[T]
  map[T2](f: T => T2): Queue[T2]
  foldLeft[T2](const: T2)(f: (T2, T) => T2): T2
  length: Int
  isEmpty: Boolean
```

### Ajout d'éléments (enqueue)

Pour ajouter des éléments dans la queue, donc dans `in`, on créé à chaque fois une nouvelle `List[T]`, contenant notre élément, puis tous les éléments de l'ancienne liste. 

```scala
val valToPrepend = 3
val currentList = List(9, 7, 2, 3)
val newList = valToPreprend::currentList

newList:
> List(3, 9, 7, 2, 3)
```

### Suppression d'éléments (dequeue)

Pour supprimer un élément de la queue (donc techniquement le premier inséré restant) on regarde d'abord l'état de notre `out : List[T]`.

```
Si out est Nil (donc vide) :
  On regarde l'état de in :
    Si in est Nil (donc vide) :
      On retourne le tuple par défaut : (Option.empty, Queue(Nil, Nil))
    Sinon, on inverse in que l'on déconstruit : 
      val head :: tail = in.reverse
    Et que l'on retourne :
      (Option(head), Queue(Nil, tail))
Sinon, si out contient un seul élément :
  On retourne simplement (Option(out.head), Queue(in, Nil)
Sinon (out contient au moins 2 éléments)
  On déconstruit out pour obtenir sa tête et le reste de la liste :
    val head :: tail = out
  Et on retourne un tuple de cette head et d'une nouvelle Queue identique, mais sans l'élément dépilé :
    (Option(head), Queue(in, tail))
```

### Récupération de la tête

Pour récupérer le dernier élément inséré, on récupère le premier élément de `in` `complexité : O(1)`, sinon le dernier élément de `out` `compléxité : O(n)`

Si les deux listes sont vides, comme la méthode est totale, on retourne `Option.empty`

### Récupération du prochain élément dépilé 

Pour récupérer le prochain élément qui sera dépilé, on récupère le permier élément de `out` `complexité : O(1)`, sinon le dernier élément de `in` `compléxité : O(n)`

Si les deux listes sont vides, comme la méthode est totale, on retourne `Option.empty`

### Transformation en Liste

Pour transformer notre `Queue[T]` en `List[T]`, il faut seulement retourner `in` concaténé avec `out` inversé. 

### Map des éléments de la Queue

Pour muter chaque élément de la Queue, on peut utiliser la fonction de `Map`. Comme on utilise les listes de `Scala` en interne, on retourne juste une nouvelle `Queue[T2]` avec `in` et `out` mappés avec la fonction qui transforme `T` en `T2` passée en paramètres.

### Fold left de la Queue

Pour faire une aggrégation sur notre `Queue[T]` avec un accumulateur constant `T2`, on va itérer récursivement vers la gauche tout en appelant la fonction passée en paramètres, permettant d'effectuer une accumulation sur chaque élément de la liste avec l'accumulateur `const` de base.

### Length de la Queue

Retourne simplement la taille de `in` + `out`

### Vérifier que la Queue est vide

Pour vérifier si la `Queue[T]` contient des éléments, on vérifie d'abord si `out` contient des éléments, puis ensuite si `in` contient des éléments et on retourne le résultat booléen de ces opérations.

## Difficultés rencontrées

Le module de programmation fonctionnelle ayant été interrompu pendant un bon moment, il a fallu se souvenir des points suivants :

- Immuabilité 
- Fonctions pures et totales

Et il a fallu aller consulter la documentation des `List[T]` de l'API de base de `Scala` pour savoir comment déconstruire une `List[T]` pour récupérer le premier élément et le reste de la liste, et pour savoir comment proprement reconstruire une nouvelle `List[T]` à partir d'un nouvel élément et de l'ancienne liste.
