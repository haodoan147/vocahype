insert into vh.roles (id, title, created_on, updated_on)
values  (1, 'user', '2023-07-12 08:05:22.731451 +00:00', null);

insert into vh.users (id, role_id, login_name, secret, encryption, first_name, last_name, birthday, gender, timezone, locale, status, login_count, created_on, updated_on)
values  (100000, 1, 'user', '123', 'None', 'New', 'User', null, null, 'GMT', 'en_GB', 1, 0, '2023-07-12 08:07:38.371824 +00:00', null);

insert into vh.pos (pos_tag, description)
values  ('UNKNOWN', 'Unknown word'),
        ('DT', 'Determiner'),
        ('QT', 'Quantifier'),
        ('CD', 'Cardinal number'),
        ('NN', 'Noun, singular'),
        ('NNS', 'Noun, plural'),
        ('NNP', 'Proper noun, singular'),
        ('NNPS', 'Proper noun, plural'),
        ('EX', 'Existential there, such as in the sentence There was a party.'),
        ('PRP', 'Personal pronoun (PP)'),
        ('PRP$', 'Possessive pronoun (PP$)'),
        ('POS', 'Possessive ending'),
        ('RBS', 'Adverb, superlative'),
        ('RBR', 'Adverb, comparative'),
        ('RB', 'Adverb'),
        ('JJS', 'Adjective, superlative'),
        ('JJR', 'Adjective, comparative'),
        ('JJ', 'Adjective'),
        ('MD', 'Modal'),
        ('VB', 'Verb, base form'),
        ('VBP', 'Verb, present tense, other than third person singular'),
        ('VBZ', 'Verb, present tense, third person singular'),
        ('VBD', 'Verb, past tense'),
        ('VBN', 'Verb, past participle'),
        ('VBG', 'Verb, gerund or present participle'),
        ('WDT', 'Wh-determiner, such as which in the sentence Which book do you like better'),
        ('WP', 'Wh-pronoun, such as which and that when they are used as relative pronouns'),
        ('WP$', 'Possessive wh-pronoun, such as whose'),
        ('WRB', 'Wh-adverb, such as when in the sentence I like it when you make dinner for me'),
        ('TO', 'The preposition to'),
        ('IN', 'Preposition or subordinating conjunction'),
        ('CC', 'Coordinating conjunction'),
        ('UH', 'Interjection'),
        ('RP', 'Particle'),
        ('SYM', 'Symbol'),
        ('$', 'Currency sign');