package database

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dao.ClassDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import model.Job
import model.Power
import model.RpgClass
import model.Student

@Database(entities = [RpgClass::class, Student::class, Job::class, Power::class], version = 1, exportSchema = false)
public abstract class AppDatabase: RoomDatabase() {

    abstract fun classDAO(): ClassDAO


    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context, scope: CoroutineScope): AppDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java,
                    "MathWorld"
                ).addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(private val scope: CoroutineScope): RoomDatabase.Callback(){
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.classDAO())
                }
            }
        }


        suspend fun populateDatabase(dao: ClassDAO){
            //val dao = database.classDAO()

            val barde = Job("Barde")
            val shapeshifter = Job("Changelin")
            val empath = Job("Empathe")
            val rogue = Job("Filou")
            val bettor = Job("Parieur")
            val spellweaver = Job("Tisse-sort")
            val hacker = Job("Hacker")

            // todo insert chaque pouvoir manuellement
            val bardPower1 = Power(1, "Barde", "Nouvel accord", "Joker de ramassage de cahier d'exercice (relancer le dé)")
            val bardPower2 = Power(2, "Barde", "Inspirer des vocations", "Avoir une musique de fond pendant une séance de cours)")
            val bardPower3 = Power(3, "Barde", "Chantons ensemble", "+1 Participation pour tout l'ilot")
            val bardPower4 = Power(4, "Barde", "The eye of the tiger", "Doubler l'XP de l'ilot pendant une journée")
            val bardPower5 = Power(5, "Barde", "Alors on danse", "Doubler la participation de l'ilot pendant une journée")
            val bardPower6 = Power(6, "Barde", "Grand festival", "Obtenir les récompenses de Numeracy Ninja de la ceinture du dessus")

            val shapeshifterPower1 = Power(1, "Changelin", "Jalousie", "Marquer quelqu'un pour la confection des ilots (pour soi)")
            val shapeshifterPower2 = Power(2, "Changelin", "Confusion", "Choisir le cahier ramassé pour la note d'ilot (pour une séance")
            val shapeshifterPower3 = Power(3, "Changelin", "Leadership", "Marquer une responsabilité pour la prochaine rotation d'ilots (pour soi)")
            val shapeshifterPower4 = Power(4, "Changelin", "Abracadabra", "+1 sur une note de cahiers d'exercices (pour l'ilot)")
            val shapeshifterPower5 = Power(5, "Changelin", "Déguisement", "Echanger le barème de deux questions après un DS (pour soi)")
            val shapeshifterPower6 = Power(6, "Changelin", "Injection shoot", "Lancer 1d6 : Succès sur +1 charge (pour soi, pour 1 semaine), Succès = XP triplè, Echec = pas d'XP ")

            val empathPower1 = Power(1, "Empathe", "Télépathie", "Donner un indice pendant un DS à un.e camarade")
            val empathPower2 = Power(2, "Empathe", "Premiers soins", "Rendre 1 Pdv à un.e camarade")
            val empathPower3 = Power(3, "Empathe", "Suspendre le temps", "Demander un sursis de sanction pour un.e camarade")
            val empathPower4 = Power(4, "Empathe", "Eveiller les sens", "Faire obtenir un nouveau pouvoir à un camarade")
            val empathPower5 = Power(5, "Empathe", "Dans ses chaussures", "Donner un indice pendant un DS à un.e camarade")
            val empathPower6 = Power(6, "Empathe", "Sacrifice utlime", "Distribuer son XP à son ilot, puis répartir à 0 XP avec gain doublé")


            val roguePower1 = Power(1, "Filou", "Ecouter aux portes", "Obtenir un indice pendant un DS")
            val roguePower2 = Power(2, "Filou", "Entrer par la porte de service", "Vérifier le manuel durant les 5 dernières minutes d'un DS")
            val roguePower3 = Power(3, "Filou", "Entrer par la grande porte", "Vérifier son cahier de leçons durant les 5 dernières minutes d'un DS")
            val roguePower4 = Power(4, "Filou", "Voler les plans", "Faire corriger une question par le professeur pendant un DS")
            val roguePower5 = Power(5, "Filou", "Altruisme", "Possibilité d'appliquer ses pouvoirs aux autres")
            val roguePower6 = Power(6, "Filou", "Braquage du Siècle", "Pouvoir regarder le sujet d'un sujet d'un DS quelques heures en avance")

            val bettorPower1 = Power(1, "Parieur", "Roulette russe", "Tirer au sort le nom d'un camarade de la classe")
            val bettorPower2 = Power(2, "Parieur", "Au petit bonheur", "TMiser de l'XP, puis lancer un d4")
            val bettorPower3 = Power(3, "Parieur", "Marcheur aléatoire", "Miser de l'XP, puis lancer un d8")
            val bettorPower4 = Power(4, "Parieur", "S'attirer les faveurs", "Lancer un d12")
            val bettorPower5 = Power(5, "Parieur", "Modeler la réalité", "Lancer un d20")
            val bettorPower6 = Power(6, "Parieur", "Sous la bonne étoile", "Tirer une carte de son paquet Divin")

            val spellweaverPower1 = Power(1, "Tisse-sort", "Time-loop", "Refaire un DTL raté")
            val spellweaverPower2 = Power(2, "Tisse-sort", "Deuxième chance", "Re-corriger un DS (6e) ou délai supplémentaire pour le rapport (5e/4e)")
            val spellweaverPower3 = Power(3, "Tisse-sort", "Booster", "Avoir un DTL supplémentaire, noté en bonus, pour augmenter sa moyenne")
            val spellweaverPower4 = Power(4, "Tisse-sort", "Hyper-booster", "Booster pour tout l'ilot +20XP en Bonus")
            val spellweaverPower5 = Power(5, "Tisse-sort", "Altruisme", "Possibilité d'appliquer ses pouvoirs aux autres")
            val spellweaverPower6 = Power(6, "Tisse-sort", "Réécrire l'histoire", "Effacer 1 malus de responsabilité pour tou l'ilot")

            val hackerPower1 = Power(1, "Hacker", "Package de données", "Obtenir des paquets des cartes ANKI tout prêts (pour soi)")
            val hackerPower2 = Power(2, "Hacker", "Trojan horse", "+1 à la note de participation (pour soi)")
            val hackerPower3 = Power(3, "Hacker", "Code source", "Accéder aux vidéos de cours pendnt les séances (pour l'ilot)")
            val hackerPower4 = Power(4, "Hacker", "Tracker", "Obtenir un indice sur une énigme 'Cadenas Lockee' (pour soi)")
            val hackerPower5 = Power(5, "Hacker", "Prendre le contrôle", "Obtenir un mot positif sur Pronote")
            val hackerPower6 = Power(6, "Hacker", "Infinite loop", "Pouvoir relancer le dé d'attribution d'un pouvoir (pour l'ilot)")

            val bardPowers: List<Power> = listOf(bardPower1, bardPower2, bardPower3, bardPower4, bardPower5, bardPower6)
            val shapeshifterPowers: List<Power> = listOf(shapeshifterPower1, shapeshifterPower2, shapeshifterPower3, shapeshifterPower4, shapeshifterPower5, shapeshifterPower6)
            val empathPowers: List<Power> = listOf(empathPower1, empathPower2, empathPower3, empathPower4, empathPower5, empathPower6)
            val roguePowers: List<Power> = listOf(roguePower1, roguePower2, roguePower3, roguePower4, roguePower5, roguePower6)
            val bettorPowers: List<Power> = listOf(bettorPower1, bettorPower2, bettorPower3, bettorPower4, bettorPower5, bettorPower6)
            val spellweaverPowers: List<Power> = listOf(spellweaverPower1, spellweaverPower2, spellweaverPower3, spellweaverPower4, spellweaverPower5, spellweaverPower6)
            val hackerPowers: List<Power> = listOf(hackerPower1, hackerPower2, hackerPower3, hackerPower4, hackerPower5, hackerPower6)

            dao.insertJob(barde)
            dao.insertJob(shapeshifter)
            dao.insertJob(empath)
            dao.insertJob(rogue)
            dao.insertJob(bettor)
            dao.insertJob(spellweaver)
            dao.insertJob(hacker)

            dao.insertPowers(bardPowers)
            dao.insertPowers(shapeshifterPowers)
            dao.insertPowers(empathPowers)
            dao.insertPowers(roguePowers)
            dao.insertPowers(bettorPowers)
            dao.insertPowers(spellweaverPowers)
            dao.insertPowers(hackerPowers)

            val sixieme = RpgClass("6e", "6e")
            dao.insertClass(sixieme)
        }
    }
}


            