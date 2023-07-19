package com.cogop.riverrougecogop;

import java.util.Random;

public class BibleVersesProvider {

    private static final String[] VERSES = {
            "Inspirational Scriptures  \n" + "For God so loved the world, that he gave his only Son, that whoever believes in him should not perish but have eternal life. - John 3:16",
            "Inspirational Scriptures \n" + "Trust in the LORD with all your heart, and do not lean on your own understanding. - Proverbs 3:5",
            "Inspirational Scriptures \n" + "The LORD is my shepherd; I shall not want. - Psalm 23:1",
            "Inspirational Scriptures \n" + "Jesus Christ is the same yesterday and today and forever. - Hebrews 13:8",
            "Inspirational Scriptures \n" + "Seek the LORD and his strength; seek his presence continually! - 1 Chronicles 16:11",
            "Inspirational Scriptures \n" + "When I am afraid, I put my trust in you. - Psalm 56:3",
            "Inspirational Scriptures \n" + "We love because he first loved us.\" - 1 John 4:19\n",
            "Inspirational Scriptures \n" + "The Lord is a refuge for the oppressed, a stronghold in times of trouble. - Psalm 9:9\n",
            "Inspirational Scriptures \n" + "I can do everything through Him who gives me strength. - Phillippians 4:13\n",
            "Inspirational Scriptures \n" + "Look to the Lord and his strength; seek his face always. - 1 Chronicles 16:11\n",
            "Inspirational Scriptures \n" + "Be on your guard; stand firm in the faith; be courageous; be strong. - 1 Corinthians 16:13\n",
            "Inspirational Scriptures \n" + "The name of the Lord is a fortified tower; the righteous run to it and are safe. - Proverbs 18:10\"\n",
            "Inspirational Scriptures \n" + "If God is for us, who can be against us? - Romans 8:31\n",
            "Inspirational Scriptures \n" + "Give thanks to the Lord, for he is good; his love endures forever. - Psalm 107:1\n",
            "Inspirational Scriptures \n" + "For I know the plans I have for you.\" declares the Lord, \"plans to give you hope and a future.\" - Jeremiah 29:11",
            "Inspirational Scriptures \n" + "Trust in the Lord with all your heart and lean not on your own understanding. In all your ways acknowledge him and he will make your paths straight.\" - Proverbs 3:5\"",
            "Inspirational Scriptures \n" + "\"Your word is a lamp onto my feet and a light for my path.\" - Psalm 119:105",
            "Inspirational Scriptures \n" + "\"But seek first the kingdom and his righteousness and all these things will be given to you as well.\" - Matthew 6:33",
            "Inspirational Scriptures \n" + "\"For everyone who calls on the name of the Lord will be saved.\" - Romans 10:13",
            "Inspirational Scriptures \n" + "\"If we confess our sins, he is faithful and just and will forgive us our sins and purify us from all unrighteousness.\" - 1 John 1:9",
            "Inspirational Scriptures \n" + "\"For it is by grace you have been saved, through faith - and not this is not from yourselves, it is the gift of God - \" Ephesians 2:8",
            "Inspirational Scriptures \n" + "\"Train up a child in the way he should go and when he is old he will not turn from it.\" - Proverbs 22:6",
            "Inspirational Scriptures \n" + "\"This is what the Lord says: 'Restrain your voice from weeping and your eyes from tears, for your work will be rewarded,' declares the Lord.\" - Jeremiah 31:16",
            "Inspirational Scriptures \n" + "\"For the Lord your God is he who goes with you to fight for you against your enemies, to give you the victory.\" - Deuteronomy 20:4",
            "Inspirational Scriptures \n" + "\"Therefore do not worry about tomorrow, for tomorrow will worry about itself. Each day has enough trouble of its own.\" - Matthew 6:34",
            "Inspirational Scriptures \n" + "\"Until now you have asked nothing in my name. Ask, and you will receive, that your joy may be full.\" - John 16:24",
            "Inspirational Scriptures \n" + "\"This is the day the Lord has made; let us rejoice and be glad in it.\" - Psalm 118:24",

            // Add more Bible verses as needed
    };

    public static String getRandomVerse() {
        Random random = new Random();
        int index = random.nextInt(VERSES.length);
        return VERSES[index];
    }
}
