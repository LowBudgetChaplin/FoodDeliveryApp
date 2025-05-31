Aplicatie Android – Food Delivery
Aplicatia FoodDeliveryApp este o aplicatie Android dedicata livrarii de mancare. Ofera suport pentru gestionarea comenzilor, utilizatorilor, restaurantelor si livrarilor printr-o interfata moderna bazata pe principiile Material Design. Proiectul utilizeaza arhitectura MVVM si integreaza functionalitati avansate precum widgeturi, validare regex si servicii Android.
Interfata cu elemente Material Design
Aplicatia contine multiple activitati si fragmente cu interfete moderne, respectand principiile Material Design.
Ecrane principale:
- LoginActivity.kt – autentificare (regex pe email pentru validare)
- RegisterActivity.kt – inregistrare cont
- SplitActivity – ecran de navigare (home screen)
- RestaurantsFragment – listare restaurante
- RestaurantPage – detalii restaurant si produse
- CartActivity – vizualizare cos curent
- OrderHistoryActivity – istoric comenzi
- CurrentOrderActivity – detalii comanda curenta
- AddRestaurantActivity / AddProductActivity – disponibile doar pentru conturi manager
- ProductListActivity – listare produse intr-un restaurant
Navigatie:
- Buton de logout
- Functionalitate de cautare restaurante
Arhitectura aplicatiei si baza de date locala
Aplicatia foloseste o arhitectura modulara care include un sistem de persistenta a datelor bazat pe ROOM. Datele sunt gestionate printr-o serie de entitati care definesc structura bazei de date locale: CartItemEntity, DeliveryLocationEntity, OrderEntity, OrderItemEntity, ProductEntity, RestaurantEntity si UserEntity. P	entru fiecare entitate exista un DAO asociat care ofera metode pentru interactiunea cu baza de date, precum: CartDao, DeliveryDao, OrderDao, ProductDao, RestaurantDao si UserDao. Baza de date este gestionata de clasa AppDatabase.
Functionalitati principale si interfata utilizatorului
Aplicatia ofera o interfata intuitiva structurata in mai multe activitati. Utilizatorul incepe cu ecranele de Login si Register, unde sunt implementate validari regex pentru formatul adresei de email. Dupa autentificare, utilizatorul este redirectionat catre ecranul principal care listeaza restaurantele disponibile. In cadrul aplicatiei exista posibilitatea de a cauta restaurante, de a vizualiza detalii despre produse, de a adauga produse in cos si de a plasa comenzi.
Pentru utilizatorii cu rol de manager, aplicatia ofera functionalitati dedicate precum adaugarea de restaurante prin AddRestaurantActivity si gestionarea produselor disponibile intr-un restaurant. De asemenea, ecranele de tip RestaurantPage si OrderHistoryActivity permit vizualizarea detaliilor comenzilor plasate.
Functionalitati avansate implementate
Aplicatia integreaza un serviciu Android pentru gestionarea notificarilor si actualizarea widgetului. Widgetul 'OrderAgainWidget' permite utilizatorului sa plaseze rapid din nou o comanda anterioara. Serviciile de localizare sunt utilizate prin integrarea cu OSRM si Nominatim pentru a obtine locatia livrarii si a estima traseul. Datele sunt gestionate printr-un Content Provider intern.
