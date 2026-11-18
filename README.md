# Projekt 1 – Rastrová grafika (2D)

Desktopová aplikace v Javě (Swing) pro interaktivní kreslení úseček a polygonů, jejich vyplňování a ořezávání.

## Ovládání

- **Horní lišta (`Tool`):**
  - `line` – režim kreslení úseček
  - `polygon` – režim zadávání vrcholů uzavřeného n-úhelníku
  - `scanline` – zapnutí/vypnutí vyplnění polygonu algoritmem Scan-line
  - `clip` – zapnutí/vypnutí ořezání úseček (obdélníkové okno) a polygonu (konvexní pětiúhelník)
  - `clear` – vymazání plátna a reset všech stavů
- **Levé tlačítko myši (stisk + tažení + uvolnění):**
  - v režimu `line`: nakreslení úsečky s pružným náhledem; při držení **`Shift`** zarovnání na vodorovnou, svislou nebo 45° osu
  - v režimu `polygon`: přidání vrcholu s pružným náhledem k prvnímu a poslednímu vrcholu
- **Pravé tlačítko myši (kliknutí):**
  - semínkové vyplnění omezené barvou pozadí
  - **`Shift` + pravé tlačítko myši**: semínkové vyplnění omezené barvou hranice (červená)
- **Klávesa `C`:** vymazání plátna, reset stavů i datových struktur

## Implementované algoritmy a struktura

- `src/App.java` – vstupní bod aplikace
- `src/gui/` – okno (`Window`), kreslící plátno (`Panel`) a lišta nástrojů (`Tool`)
- `src/controller/Controller2D.java` – obsluha myši, klávesnice, tlačítek a vykreslení scény
- `src/raster/` – rozhraní `Raster` a implementace `RasterBufferedImage`
- `src/model/line/` – `Line` a `LineRasterizer` (algoritmus DDA)
- `src/model/polygon/` – `Polygon` a `PolygonRasterizer`
- `src/fill/` – `Seedfilter` (zásobníkový `ArrayDeque`) a `Scanline`
- `src/clip/` – `CohenSutherland` (ořezání úsečky) a `SutherlandHodgman` (ořezání polygonu konvexním pětiúhelníkem s automatickou detekcí orientace)
