/*
TP3 DE MARTIN SENÉCAL
DA:1737787
 */
package pkgTP3_MARTIN_SENECAL;

//Importation Java:
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class tp3 {

    public static void main(String[] args) throws IOException {
        boolean continu = true; //Variable déterminant si on reste dans la boucle ou non.
        do {
            boolean existeClient; //déterminant si client entré existe.
            int choixUsers/*choix options*/, numeroClient/*choisi par users*/, somme /*argent dépensé*/;
            int due /*somme remboursé*/, sommeDue/*à remboursé*/, ligneNumeroClient = 0 /*#ligne du client*/;
            bienvenu(); //Appeler Méthode bienvenu
            int nbClients = nombreClients(); //Méthode calculant le nbre de clients.
            int nbColonnes = nombreColonnes();//Méthode calculant le nbre de colonnes (mois)
            int matDonnees[][] = new int[nbClients][nbColonnes];//Matrice contenant toutes valeurs
            int matStatistiques[][] = new int[nbClients][4];//Matrice contenant valeurs du cas 2
            matDonnees = lireFichier(nbClients, nbColonnes); //Méthode transformant le fichier en matrice
            choixUsers = menu(); //Méthode qui choisis quel options choisir.
            matStatistiques = statistiqueMatrice(matDonnees, nbClients); //Méthode créant matrice (somme-remboursé-due)

            switch (choixUsers) { //Dépendant du choix de programmes, on exécute le code.
                case 1: //Statistique d'un client
                    do {
                        numeroClient = numeroClientMeth(); //Méthode demandant numéro.
                        existeClient = existeClient(numeroClient, nbClients, matDonnees);//Vérifier si numéro existe
                        for (int i = 0; i < matDonnees.length && existeClient; i++) { //Trouver position du numéro (rangée)
                            if (numeroClient == matDonnees[i][0]) {
                                existeClient = true;
                                ligneNumeroClient = i;
                            }

                        }
                    } while (existeClient == false);
                    somme = calculSommesClients(numeroClient, matDonnees); //Méthode calculant la somme des dépenses.
                    due = calculRemboursementClients(numeroClient, matDonnees);//Méthode calculant la somme des remboursements
                    sommeDue = calculSommesDues(somme, due);//Méthode calculant l'argent total restant à payer.
                    System.out.println("*************************************");
                    for (int i = 0; i < matDonnees[ligneNumeroClient].length; i++) {
                        System.out.print(matDonnees[ligneNumeroClient][i] + " "); //Afficher les valeurs de la rangée.

                    }
                    System.out.println(" ");

                    System.out.println("*************************************");
                    System.out.println("La somme total est de: " + somme);
                    System.out.println("La somme payé est de: " + due);
                    System.out.println("La somme dues est de: " + sommeDue);
                    System.out.println("*************************************");
                    continu = continuer(); //Méthode demandant s'il veut quitter ou non.
                    break;

                case 2://Fichiers statistique des crédits 

                    ecrireFichier(matStatistiques, nbClients); //Méthode créant un fichier avec les stats 
                    continu = continuer(); //Méthode demandant s'il veut quitter ou non.
                    break;

                case 3://Chercher le meilleur client

                    meilleurClient(matStatistiques); //Méthode cherchant le meilleur client et afficher.
                    continu = continuer();//Méthode demandant s'il veut quitter ou non.

                    break;
                case 4://Chercher le mauvais payeur

                    mauvaisPayeur(matStatistiques);//Méthode cherchant le mauvais payeur.
                    continu = continuer();//Méthode demandant s'il veut quitter ou non.

                    break;

                case 5://Quitter
                    continu = false; //On ne refais plus la boucle.
                    break;

            }
        } while (continu == true);//Tant que boucle=true, on la refais.
        System.out.println("***************************************************");
        System.out.println("*À bientôt! Merci d'avoir utilisé le programme!   *");
        System.out.println("***************************************************");
    }

    public static void bienvenu() { //Méthode: message de bienvenue
        System.out.println("***************************************************");
        System.out.println("* Bienvenue dans l'application de comptabilité!   *");
        System.out.println("***************************************************");
    }

    public static int nombreClients() throws IOException { //Méthode cherchant le nbre de client.
        String ligne;
        int nbClients = 0;

        BufferedReader fichierLire = new BufferedReader(new FileReader("2016.txt"));

        while ((ligne = fichierLire.readLine()) != null) { //Tant qu'il a des lignes, on additionne.
            nbClients++;
        }
        nbClients = nbClients - 1; //Afin d'enlever les mois.
        return nbClients;
    }

    public static int nombreColonnes() throws IOException { //Méthode calculant le nbre de colonne (mois)
        BufferedReader fichierLire = new BufferedReader(new FileReader("2016.txt"));
        String ligne = fichierLire.readLine();

        String nbColonnes[] = ligne.split("\t"); //Création tableaux sur une ligne
        fichierLire.close();

        return nbColonnes.length; //Nombre de mots dans une rangée=nbColonnes
    }

    //Méthode servant à lire le fichier 2016 afin de créer une matrice
    public static int[][] lireFichier(int nbClients, int nbColonnes) throws FileNotFoundException, IOException {
        int matDonnees[][] = new int[nbClients][nbColonnes]; //Taille de la matrice.
        String ligne;
        int i = 0, j;

        BufferedReader fichierLire = new BufferedReader(new FileReader("2016.txt"));
        fichierLire.readLine();
        while ((ligne = fichierLire.readLine()) != null) { //Chaque mot du fichier=case de la matrice.
            String tab[] = ligne.split("\t"); //Chaque nombres est séparé d'un espace.

            for (j = 0; j < tab.length; j++) {
                matDonnees[i][j] = Integer.parseInt(tab[j]); //Intégrer valeurs dans matrice.
            }
            i++;
        }
        fichierLire.close();//Fermer le fichier.

        return matDonnees;//Renvoyer la matrice contenant toutes les valeurs.

    }

    //Méthode #3: Afficher le menu avec les choix possible et demander choix du users.
    public static int menu() {
        int choixUsers; //choix (1 à 5)
        Scanner entreeNb = new Scanner(System.in); //Entrée de nombres.
        //Afficher les choix  pour l'utilisateur 
        System.out.println("**********************************************");
        System.out.println("*Que voulez-vous faire?                      *"
                + "\n*     -----------------                      *");
        System.out.println("* 1. Statistique d'un client                 *"
                + "\n* 2. Fichiers statistique des crédits        *"
                + "\n* 3. Chercher le meilleur client             *"
                + "\n* 4. Chercher le mauvais payeur              *"
                + "\n* 5. Quitter                                 *");
        System.out.println("**********************************************");
        do { //Entrer un de ces choix seulement.
            System.out.println("Entrez un choix valide:");
            choixUsers = entreeNb.nextInt();
        } while (choixUsers < 1 || choixUsers > 5);
        return choixUsers; //valeur 'choix' sera retourné aux main afin de faire le switch
    }

    public static boolean existeClient(int numeroClient, int nbreClient, int[][] matriceID) { //Vérifier si ID client existe.

        boolean existeClient = false;
        int j;

        for (j = 0; j < nbreClient; j++) { //Vérifier chaque premier nbre des rangées.
            if (numeroClient == matriceID[j][0]) { //Si le numéro se retrouve; c'est vrai.
                existeClient = true;
            }
        }

        return existeClient;
    }

    public static int numeroClientMeth() { //Méthode demandant d'inscrire un numéro de client.
        Scanner entreeNb = new Scanner(System.in);
        System.out.println("***************************************************");
        System.out.println("* Veuillez entrer un numéro de client valide!     *");
        System.out.println("***************************************************");
        int numeroClient = entreeNb.nextInt();

        return numeroClient;

    }

    public static int calculSommesClients(int numeroClient, int matrice[][]) { //Méthode calculant sommes totals des achats.
        int totalAchats = 0, i = 0, j = 0;

        for (i = 0; i < matrice.length; i++) { //Boucle qui vérifie chaque rangée.
            if (numeroClient == matrice[i][0]) { //Si #choisi=1er numéro de la rangée alors..
                for (j = 1; j < matrice[i].length; j = j + 2) { //On vérifie à partir de la 2e colonne avec bond de 2: somme
                    totalAchats = totalAchats + matrice[i][j]; //Aditionner le total des achats.

                }
            }

        }

        return totalAchats;//Retourner le total des achats.
    }

    public static int calculRemboursementClients(int numeroClient, int matrice[][]) { //Méthodes calculant la somme des remboursements.
        int i, j, totalPaye = 0;

        for (i = 0; i < matrice.length; i++) {//Boucle vérifiant chaque rangée.
            if (numeroClient == matrice[i][0]) { //Si #choisi=1er numéro da la rangée alors..
                for (j = 2; j < matrice[i].length; j = j + 2) { //On vérifie à partir de la 3e colonne (bond de 2) :la somme reboursé.
                    totalPaye = totalPaye + matrice[i][j];//Additionner le total des remboursements.

                }
            }
        }
        return totalPaye;//Additionner le total des remboursements.

    }

    public static int calculSommesDues(int somme, int due) { //Méthode calculant la somme à payer.
        int sommeDue;
        sommeDue = somme - due; //Calculer somme due.
        return sommeDue;
    }

    public static int[][] statistiqueMatrice(int matDonnees[][], int nbClients) { //Méthode créant une matrice: statistiques des clients.

        int matStats[][] = new int[nbClients][4]; //Taille du tableaux 2d
        int i, achats, paye, due;//déclaration

        for (i = 0; i < matStats.length; i++) { //1ère colonne: on rentre les numéros des clients.
            matStats[i][0] = matDonnees[i][0]; //copie de l'autre matrice.
        }

        for (i = 0; i < nbClients; i++) { //2e colonne: on rentre les sommes.
            achats = calculSommesClients(matStats[i][0], matDonnees); //Méthode du calcul sommes pour chaque ID.
            matStats[i][1] = achats; //affectation valeurs matrice à achats.
        }

        for (i = 0; i < nbClients; i++) { //3e colonne: rentrer les remboursements.
            paye = calculRemboursementClients(matStats[i][0], matDonnees); //Méthode calculant remboursements pour chaque ID
            matStats[i][2] = paye;//affecter valeurs payé à matrice.
        }

        for (i = 0; i < nbClients; i++) {//4e colonne: argent à rembourser.
            achats = calculSommesClients(matDonnees[i][0], matDonnees);//Méthode calculant achats.
            paye = calculRemboursementClients(matStats[i][0], matDonnees);//Méthode calculant remboursements.
            due = calculSommesDues(achats, paye);//Méthode calculant ''à rembourser''
            matStats[i][3] = due;//affecter sommes dues à la matrice.
        }

        return matStats; //Retourner matrice complété

    }

    public static PrintWriter ecrireFichier(int matStats[][], int nbClients) throws FileNotFoundException { //Méthode créant un fichier grâce à la matrice statistiques.
        int i, achats = 0, paye = 0, due = 0;
        PrintWriter fichierSortie = new PrintWriter(new FileOutputStream("2016_Stat.txt", false)); //Création du fichier (false:toujours un nouveau).
        fichierSortie.println("Client:\tSomme:\tPayée:\tReste:");
        for (i = 0; i < matStats.length; i++) { //Afficher chaque valeurs de la matrice (selon l'ordre)
            for (int j = 0; j < matStats[i].length; j++) {
                fichierSortie.print(matStats[i][j] + "\t");
            }
            fichierSortie.println("\t");
        }
        for (i = 0; i < nbClients; i++) { //Calculer sommes totales de chaque colonnes.
            achats = matStats[i][1] + achats;//sommes achats.

            paye = matStats[i][2] + paye;//sommes remboursés

            due = matStats[i][3] + due;//sommes à rembourser.

        }
        //affichage.
        fichierSortie.println("");
        fichierSortie.println("Total Achats:" + achats);
        fichierSortie.println("Total Payé:" + paye);
        fichierSortie.println("Sommes Dues:" + due);
        fichierSortie.close();
        return fichierSortie; //retourner un fichier rempli.

    }

    public static void meilleurClient(int matStats[][]) { //Méthode cherchant le client avec + de dépenses.
        int j, max = matStats[0][1]; //Initialiser le max à première valeur de la matrice.
        int compteur = 0;//Afin de trouver la position du meilleur.
        for (j = 0; j < matStats.length; j++) {
            if (matStats[j][1] > max) {//Chercher la valeur max.

                compteur = j; //Position du maximum
            }
        }
        //Afficher le meilleur avec la somme de ses achats.
        System.out.println("Le client " + matStats[compteur][0] + " a dépensé le plus soit: " + matStats[compteur][1] + "$");

    }

    public static void mauvaisPayeur(int matStats[][]) {//Méthode cherchant le client mauvais payeur.
        int j, max = matStats[0][3];//Initialiser le max à première valeur de 4e colonne.
        int compteur = 0;//Afin de trouver la position du pire.
        for (j = 0; j < matStats.length; j++) { //Chercher la valeur max.
            if (matStats[j][3] > max) {
                compteur = j; //Position du max.
            }
        }
        //Afficher le pire payeur avec ses dettes.
        System.out.println("Le client " + matStats[compteur][0] + " a le plus de dettes soit: " + matStats[compteur][3] + "$");

    }

    public static boolean continuer() { //Méthode demandant si utilisateur veut continuer ou arrêter.
        boolean reponse;
        Scanner entreeMot = new Scanner(System.in);

        System.out.println("Voulez-vous continuer?");
        reponse = entreeMot.nextBoolean(); //Réponses sera soit un true ou un false.

        return reponse;//Retourner un boolean.

    }

}
