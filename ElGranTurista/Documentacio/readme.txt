Index:

- Notacions eclipse
- Missatges github
- Info requisits mapa
- Base de dades

-------------------------
-------------------------

-----------------
Notacions eclipse
-----------------

TextView - lbl
EditText - txt
Button - cmb
RadioButton - rdg
RadioGroup - rdb
CheckBox - chb
Spinner - spn
Layout - lyt
ScrollView - scv
Image - img

exemple scrollview opinions -> id = scvOpinions

----------------
Missatges github
----------------

afegit:
alterat: x.java (afegida/elimat/optimitzat)
optimitzat: 
eliminat: 

-------------------
Info requisits mapa
-------------------

- Per a que el mapa es pugui carregar es necessita
tenir la app de Google Maps.
- S'ha d'incorporar la llibreria "google-play-services_lib"
- Es necessita tenir els paquets "Google Play services" i "Google Repository"
- (Teoricament no passa) Pot passar que al canviar d'ordinador s'hagi de
crear una nova clau per treballar amb l'accés als 
mapes, els passos estan descrits aqui: http://hmkcode.com/getting-android-google-maps-v2-api-key/

-------------
Base de dades
-------------

Qualsevol activity que necessiti base de dades ha d'escriure "InternalDB db = InternalDB.getInstance(this);"
Nota: Si és un fragment s'ha de passar getActivity().
Per accedir a un monument: db.selectMonument(int id); returns Monument
Per accedir a tots els monuments: db.selectAllMonuments(); returns ArrayList<Monument>
Per accedir a els monuments amb X tag: db.getMonumentsTag(String tag); returns ArrayList<Monument>
