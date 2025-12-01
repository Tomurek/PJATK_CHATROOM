# Zadanie: Uzupełnienie manifestu Kubernetes Service

Twoim zadaniem jest poprawne uzupełnienie manifestu Kubernetes.

## Instrukcje do uzupełnienia:

### 1. `kind`

Ustaw wartość:

    Service

### 2. `metadata.name`

Nazwa serwisu:

    chat-app

### 3. `metadata.labels.app`

Etykieta musi odpowiadać nazwie aplikacji:

    chat-app

### 4. `spec.type`

Aby serwis obsługiwał load balancing i mógł być wystawiony na zewnątrz
klastra, użyj:

    LoadBalancer

### 5. `spec.selector.app`

Selector dopasowujący pody:

    chat-app

### 6. `spec.ports.port`

Zewnętrzny port usługi:

    80

### 7. `spec.ports.targetPort`

Port kontenera aplikacji:

    8080
