insert into vh.roles (id, title, created_on, updated_on)
values  (1, 'user', '2023-07-12 08:05:22.731451 +00:00', null)
ON CONFLICT DO NOTHING;

insert into vh.users (id, role_id, login_name, secret, encryption, first_name, last_name, birthday, gender, timezone, locale, status, login_count, created_on, updated_on)
values  (100000, 1, 'user', '123', 'None', 'New', 'User', null, null, 'GMT', 'en_GB', 1, 0, '2023-07-12 08:07:38.371824 +00:00', null)
ON CONFLICT DO NOTHING;

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
        ('$', 'Currency sign')
ON CONFLICT DO NOTHING;

insert into vh.word_comprehension_levels (id, reinforce_interval, description)
values  (1, '0 years 0 mons 0 days 0 hours 0 mins 0.0 secs', 'to learn'),
        (2, '0 years 0 mons 1 days 0 hours 0 mins 0.0 secs', 'learning'),
        (3, '0 years 0 mons 3 days 0 hours 0 mins 0.0 secs', 'learning'),
        (4, '0 years 0 mons 7 days 0 hours 0 mins 0.0 secs', 'learning'),
        (6, '0 years 0 mons 14 days 0 hours 0 mins 0.0 secs', 'learning'),
        (7, '0 years 0 mons 30 days 0 hours 0 mins 0.0 secs', 'learning'),
        (8, '0 years 2 mons 0 days 0 hours 0 mins 0.0 secs', 'learning'),
        (9, '0 years 3 mons 0 days 0 hours 0 mins 0.0 secs', 'learning'),
        (10, '0 years 6 mons 0 days 0 hours 0 mins 0.0 secs', 'learning'),
        (11, '1 years 0 mons 0 days 0 hours 0 mins 0.0 secs', 'learning'),
        (12, null, 'mastered'),
        (13, null, 'ignore')
ON CONFLICT DO NOTHING;