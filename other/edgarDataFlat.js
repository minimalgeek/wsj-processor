db.getCollection('edgar_data').aggregate([
    {
        $unwind: "$edgarXML.nonDerivativeTable"
    },
    {
        $project: {
            _id : 0,
            tradingSymbol : 1,
            periodOfReport : "$edgarXML.periodOfReport",
            company : "$edgarXML.issuer.issuerName",
            reportingOwner : "$edgarXML.reportingOwner.reportingOwnerId.rptOwnerName",
            isDirector : "$edgarXML.reportingOwner.reportingOwnerRelationship.isDirector",
            isOfficer : "$edgarXML.reportingOwner.reportingOwnerRelationship.isOfficer",
            isTenPercentOwner : "$edgarXML.reportingOwner.reportingOwnerRelationship.isTenPercentOwner",
            isOther : "$edgarXML.reportingOwner.reportingOwnerRelationship.isOther",
            officerTitle : "$edgarXML.reportingOwner.reportingOwnerRelationship.officerTitle",
            securityTitle : "$edgarXML.nonDerivativeTable.securityTitle.value",
            transactionDate : "$edgarXML.nonDerivativeTable.transactionDate.value",
            transactionFormType : "$edgarXML.nonDerivativeTable.transactionCoding.transactionFormType",
            transactionCode : "$edgarXML.nonDerivativeTable.transactionCoding.transactionCode",
            equitySwapInvolved : "$edgarXML.nonDerivativeTable.transactionCoding.equitySwapInvolved",
            transactionShares : "$edgarXML.nonDerivativeTable.transactionAmounts.transactionShares.value",
            transactionPricePerShare : "$edgarXML.nonDerivativeTable.transactionAmounts.transactionPricePerShare.value",
            transactionAcquiredDisposedCode : "$edgarXML.nonDerivativeTable.transactionAmounts.transactionAcquiredDisposedCode.value",
            sharesOwnedFollowingTransaction : "$edgarXML.nonDerivativeTable.postTransactionAmounts.sharesOwnedFollowingTransaction.value",
            directOrIndirectOwnership : "$edgarXML.nonDerivativeTable.ownershipNature.directOrIndirectOwnership.value",
            signatureName : "$edgarXML.ownerSignature.signatureName",
            signatureDate : "$edgarXML.ownerSignature.signatureDate"
        }
    },    { $out: "edgar_data_flat" }
]);