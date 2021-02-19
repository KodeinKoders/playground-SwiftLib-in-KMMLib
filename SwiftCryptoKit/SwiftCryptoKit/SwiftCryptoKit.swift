import Foundation
import CryptoKit


@objc public class DataResult: NSObject {
    
    @objc public private(set) var success: Data?
    @objc public private(set) var failure: Error?
    
    private init(_ success: Data?, _ failure: Error?) {
        super.init()
        self.success = success
        self.failure = failure
    }
    
    public convenience init(success: Data) {
        self.init(success, nil)
    }
    public convenience init(failure: Error) {
        self.init(nil, failure)
    }
    public convenience init(_ block: () throws -> Data) {
        do {
            let data = try block()
            self.init(success: data)
        } catch {
            self.init(failure: error)
        }
    }
}

@objc public class SwiftCryptoKit : NSObject {
    
    @objc public class func chachapoly_encrypt(key: Data, nonce: Data, authenticatedData: Data, plaintext: Data) -> DataResult {
        DataResult {
            let symKey = SymmetricKey(data: key)
            let chaNonce: ChaChaPoly.Nonce = try ChaChaPoly.Nonce(data: nonce)
            let sealedBox: ChaChaPoly.SealedBox = try ChaChaPoly.seal(plaintext, using: symKey, nonce: chaNonce, authenticating: authenticatedData)
            return sealedBox.ciphertext + sealedBox.tag
        }
    }

    @objc public class func chachapoly_decrypt(key: Data, nonce: Data, authenticatedData: Data, ciphertextAndTag: Data) -> DataResult {
        DataResult {
            let tagIndex = ciphertextAndTag.endIndex - 16
            let ciphertext = ciphertextAndTag[ciphertextAndTag.startIndex ..< tagIndex]
            let tag = ciphertextAndTag[tagIndex ..< ciphertextAndTag.endIndex]

            let symKey = SymmetricKey(data: key)
            let chaNonce: ChaChaPoly.Nonce = try ChaChaPoly.Nonce(data: nonce)
            let sealedBox: ChaChaPoly.SealedBox = try ChaChaPoly.SealedBox(nonce: chaNonce, ciphertext :ciphertext, tag: tag)
            return try ChaChaPoly.open(sealedBox, using: symKey, authenticating: authenticatedData)
        }
    }
}
