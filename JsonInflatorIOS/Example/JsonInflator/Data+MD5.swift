//
//  Data+MD5.swift
//  Json inflator example
//
//  Extends data to easily calculcate the MD5 hash of it
//

import Foundation
import CommonCrypto

extension Data {

    func md5() -> String {
        // Generate hash
        let hash = self.withUnsafeBytes { (bytes: UnsafeRawBufferPointer) -> [UInt8] in
            var hash = [UInt8](repeating: 0, count: Int(CC_MD5_DIGEST_LENGTH))
            CC_MD5(bytes.baseAddress, CC_LONG(self.count), &hash)
            return hash
        }
        
        // Return md5 hash string formatted as hexadecimal
        return hash.map { String(format: "%02hhx", $0) }.joined()
    }
    
}
