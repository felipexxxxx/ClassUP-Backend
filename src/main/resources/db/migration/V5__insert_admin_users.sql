INSERT INTO users (
    nome_completo,
    matricula,
    data_nascimento,
    email,
    cpf,
    senha,
    role,
    criado_em,
    modificado_em
)
SELECT * FROM (
    SELECT
        'Administrador do Sistema' AS nome_completo,
        'admin' AS matricula,
        '1990-01-01' AS data_nascimento,
        'admin@classup.com' AS email,
        '00000000000' AS cpf,
        ${ADMIN_PASSWORD} AS senha,
        'ADMIN' AS role,
        NOW() AS criado_em,
        NOW() AS modificado_em
) AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'admin@classup.com'
);
