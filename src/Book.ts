export interface Book {
  isbn: string; // Utilisation de string pour l'ID dans votre app (ça peut correspondre à un string ou un number selon le cas)
  title: string;
  author: string;
  date: string;
  coordonnees: string[];
  emplacement: string;
  favoris: boolean;
}

