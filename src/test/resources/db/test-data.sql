

-- Inserts para a tabela boletomodel
INSERT INTO public.boletomodel (jurosatraso, multa, valor, dataemissao, datavencimento, boletoid, beneficiariocnpjcpf, beneficiarioendereco, beneficiarionome, codigoautenticacao, codigobanco, codigodebarras, identificacaoboleto, informacoescontato, instrucoespagamento, linhadigitavel, localpagamento, logobanco, numerodocumento, sacadocnpjcpf, sacadoendereco, sacadonome)
VALUES
(0.05, 2.0, 100.0, '2023-10-26 12:00:00', '2023-11-26 12:00:00', '12345678-1234-1234-1234-123456789012', '12345678901', 'Endereço Beneficiário', 'Nome Beneficiário', 'ABCDE12345', '001', '1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234', 'Identificação do Boleto', 'Informações de Contato', 'Instruções de Pagamento', '1234.5678.9012 3456.7890.1234 5678.9012 3456.7890.1234.5678', 'Local de Pagamento', 'Logo Banco', '1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234', '12345678901', 'Endereço Sacado', 'Nome Sacado');

-- Inserts para a tabela customermodel
INSERT INTO public.customermodel (borndate, registerdate, customerid, userid)
VALUES
('1990-01-01 00:00:00', '2023-10-26 12:00:00', '12345678-1234-1234-1234-123456789013', 'userId');

INSERT INTO public.customermodel (borndate, registerdate, customerid, userid)
VALUES
('1990-01-01 00:00:00', '2023-10-26 12:00:00', '12345678-1234-1234-1234-123456789022', 'userId1');

-- Inserts para a tabela cardmodel
INSERT INTO public.cardmodel (cardtype, cardid, card_id, cardholdername, cardnumber, cvv, expirationdate)
VALUES
(0, '12345678-1234-1234-1234-123456789014', '12345678-1234-1234-1234-123456789013', 'Nome do Titular', '1234 5678 9012 3456', '123', '12/25');

-- Inserts para a tabela paypalmodel
INSERT INTO public.paypalmodel (paypalid, descricao, email, identificadortransacao)
VALUES
('12345678-1234-1234-1234-123456789015', 'Descrição PayPal', 'paypal@example.com', 'ABCDE12345');

-- Inserts para a tabela pixmodel
INSERT INTO public.pixmodel (pixid, chavepix, descricao, identificadortransacao)
VALUES
('12345678-1234-1234-1234-123456789016', 'chave-pix-12345', 'Descrição Pix', 'ABCDE12345');

-- Inserts para a tabela paymentmodel
INSERT INTO public.paymentmodel (dicountamount, haserrors, paymentmethod, paymentstate, totalamount, registerdate, boleto_model_id, card_model_id, customer_id, paymentid, paypal_model_id, pix_model_id, orderid)
VALUES
(5.0, false, 1, 2, 100.0, '2023-10-26 12:00:00', '12345678-1234-1234-1234-123456789012', '12345678-1234-1234-1234-123456789014', '12345678-1234-1234-1234-123456789013', '12345678-1234-1234-1234-123456789017', '12345678-1234-1234-1234-123456789015', '12345678-1234-1234-1234-123456789016', 'orderId');

INSERT INTO public.paymentmodel (dicountamount, haserrors, paymentmethod, paymentstate, totalamount, registerdate, boleto_model_id, card_model_id, customer_id, paymentid, paypal_model_id, pix_model_id, orderid)
VALUES
(5.0, false, 1, 2, 100.0, '2023-10-26 12:00:00', '12345678-1234-1234-1234-123456789012', '12345678-1234-1234-1234-123456789014', '12345678-1234-1234-1234-123456789013', 'e8f98317-d29a-453c-8557-47d3f26040d1', '12345678-1234-1234-1234-123456789015', '12345678-1234-1234-1234-123456789016', 'orderId');

INSERT INTO public.paymentmodel (dicountamount, haserrors, paymentmethod, paymentstate, totalamount, registerdate, boleto_model_id, card_model_id, customer_id, paymentid, paypal_model_id, pix_model_id, orderid)
VALUES
(5.0, false, 1, 2, 100.0, '2023-10-26 12:00:00', '12345678-1234-1234-1234-123456789012', '12345678-1234-1234-1234-123456789014', '12345678-1234-1234-1234-123456789013', '12345678-1234-1234-1234-123456789023', '12345678-1234-1234-1234-123456789015', '12345678-1234-1234-1234-123456789016', 'orderId');


-- Inserts para a tabela invoicemodel
INSERT INTO public.invoicemodel (discountamount, totalamount, invoicedate, customer_id, invoiceid, paymentid, buyeraddress, buyername, invoicenumber, selleraddress, sellername, itemsid)
VALUES
(10.0, 200.0, '2023-10-26 12:00:00', '12345678-1234-1234-1234-123456789013', '12345678-1234-1234-1234-123456789018', '12345678-1234-1234-1234-123456789017', 'Endereço Comprador', 'Nome Comprador', '12345', 'Endereço Vendedor', 'Nome Vendedor', ARRAY['item1', 'item2']);


INSERT INTO public.invoicemodel (discountamount, totalamount, invoicedate, customer_id, invoiceid, paymentid, buyeraddress, buyername, invoicenumber, selleraddress, sellername, itemsid)
VALUES
(10.0, 200.0, '2023-10-26 12:00:00', '12345678-1234-1234-1234-123456789013', '12345678-1234-1234-1234-123456789024', '12345678-1234-1234-1234-123456789017', 'Endereço Comprador', 'Nome Comprador', '12345', 'Endereço Vendedor', 'Nome Vendedor', ARRAY['item1', 'item2']);
