/**
 * 发票类型-常量定义
 * 
 * @returns
 */
function InvoiceType() {

}

/*
 * 以下为常量定义 0：专用发票 2：普通发票 12：机动车票 51：电子发票
 */

InvoiceType.FPZL_SPECIAL_INVOICE = "0"; // 专用发票
InvoiceType.FPZL_NORMAL_INVOICE = "2"; // 普通发票
InvoiceType.FPZL_VEHICLE_INVOICE = "12"; // 机动车发票
InvoiceType.FPZL_ELEC_INVOICE = "51"; // 电子发票

/*
 * 	根据发票类型返回发票类型名称
 * 	invoiceType--->invoiceTypeName
 * 	
 */
InvoiceType.getInvoiceName = function(invoiceType) {
	var invoiceTypeName="";
	switch (invoiceType) {
	case InvoiceType.FPZL_SPECIAL_INVOICE:
		invoiceTypeName="专用发票";
		break;
	case InvoiceType.FPZL_NORMAL_INVOICE:
		invoiceTypeName="普通发票";
		break;
	case InvoiceType.FPZL_VEHICLE_INVOICE:
		invoiceTypeName="机动车发票";
		break;
	case InvoiceType.FPZL_ELEC_INVOICE:
		invoiceTypeName="电子发票";
		break;
	}
	return invoiceTypeName;
}
